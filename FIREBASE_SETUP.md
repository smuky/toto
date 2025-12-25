# Firebase Anonymous Authentication Setup

This guide explains how to configure Firebase Anonymous Authentication for your Spring Boot application.

## Prerequisites

- A Firebase project (create one at https://console.firebase.google.com/)
- Firebase Anonymous Authentication enabled in your project

## Step 1: Enable Anonymous Authentication in Firebase

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Navigate to **Authentication** > **Sign-in method**
4. Click on **Anonymous** and toggle it to **Enabled**
5. Click **Save**

## Step 2: Download Service Account Credentials

1. In Firebase Console, click the **gear icon** (⚙️) next to "Project Overview"
2. Select **Project settings**
3. Go to the **Service accounts** tab
4. Click **Generate new private key**
5. Click **Generate key** in the confirmation dialog
6. Save the downloaded JSON file as `firebase-service-account.json`

## Step 3: Configure Your Application

### Option A: Using Classpath (Recommended for Development)

1. Copy `firebase-service-account.json` to `src/main/resources/`
2. Add to `.gitignore`:
   ```
   **/firebase-service-account.json
   ```
3. Update `src/main/resources/application.yml`:
   ```yaml
   firebase:
     credentials:
       path: classpath:firebase-service-account.json
   ```

### Option B: Using Environment Variable (Recommended for Production)

1. Store `firebase-service-account.json` in a secure location on your server
2. Set environment variable:
   ```bash
   export FIREBASE_CREDENTIALS_PATH=file:/absolute/path/to/firebase-service-account.json
   ```
3. The application will automatically pick it up from `application.yml`:
   ```yaml
   firebase:
     credentials:
       path: ${FIREBASE_CREDENTIALS_PATH:}
   ```

### Option C: Using Application Default Credentials (ADC)

Set the `GOOGLE_APPLICATION_CREDENTIALS` environment variable:
```bash
export GOOGLE_APPLICATION_CREDENTIALS=/absolute/path/to/firebase-service-account.json
```

## Step 4: Client-Side Implementation

### Web (JavaScript)

```javascript
import { getAuth, signInAnonymously } from "firebase/auth";

const auth = getAuth();
signInAnonymously(auth)
  .then((userCredential) => {
    // Get the ID token
    return userCredential.user.getIdToken();
  })
  .then((idToken) => {
    // Use this token in your API requests
    fetch('http://localhost:8080/your-endpoint', {
      headers: {
        'Authorization': `Bearer ${idToken}`
      }
    });
  });
```

### iOS (Swift)

```swift
import FirebaseAuth

Auth.auth().signInAnonymously { authResult, error in
    guard let user = authResult?.user else { return }
    
    user.getIDToken { idToken, error in
        guard let token = idToken else { return }
        
        // Use this token in your API requests
        var request = URLRequest(url: URL(string: "http://localhost:8080/your-endpoint")!)
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
    }
}
```

### Android (Kotlin)

```kotlin
import com.google.firebase.auth.FirebaseAuth

FirebaseAuth.getInstance().signInAnonymously()
    .addOnSuccessListener { authResult ->
        authResult.user?.getIdToken(true)?.addOnSuccessListener { result ->
            val idToken = result.token
            
            // Use this token in your API requests
            val request = Request.Builder()
                .url("http://localhost:8080/your-endpoint")
                .addHeader("Authorization", "Bearer $idToken")
                .build()
        }
    }
```

## Step 5: Using the UID in Your Controllers

Once configured, you can access the Firebase UID in your Spring controllers:

### Using Principal

```java
@GetMapping("/user/profile")
public ResponseEntity<UserProfile> getProfile(Principal principal) {
    String uid = principal.getName();
    // Use uid to fetch user data
    return ResponseEntity.ok(userService.getProfile(uid));
}
```

### Using Authentication

```java
@PostMapping("/user/data")
public ResponseEntity<Void> saveData(
    @RequestBody UserData data,
    Authentication authentication
) {
    String uid = authentication.getName();
    userService.saveData(uid, data);
    return ResponseEntity.ok().build();
}
```

### Using Request Attribute

```java
@GetMapping("/user/settings")
public ResponseEntity<Settings> getSettings(HttpServletRequest request) {
    String uid = (String) request.getAttribute("firebaseUid");
    return ResponseEntity.ok(settingsService.get(uid));
}
```

## Security Notes

1. **Never commit** `firebase-service-account.json` to version control
2. Always add it to `.gitignore`
3. In production, use environment variables or secure secret management
4. Firebase ID tokens expire after 1 hour - clients should refresh them
5. The current security configuration allows all requests (`permitAll()`)
   - To require authentication, change `SecurityConfig.java`:
     ```java
     .authorizeHttpRequests(auth -> auth
         .requestMatchers("/public/**").permitAll()
         .anyRequest().authenticated()
     )
     ```

## Troubleshooting

### "Default credentials not found" error
- Ensure `firebase.credentials.path` is set in `application.yml`
- Verify the JSON file exists at the specified path
- Check file permissions

### "Invalid ID token" (401 Unauthorized)
- Token may have expired (refresh on client)
- Verify the token is from the correct Firebase project
- Ensure Anonymous Auth is enabled in Firebase Console

### "FirebaseApp already exists"
- This is normal on hot-reload during development
- The code handles this automatically

## Testing with cURL

```bash
# Get an ID token from your client app, then:
curl -H "Authorization: Bearer YOUR_FIREBASE_ID_TOKEN" \
     http://localhost:8080/your-endpoint
```
