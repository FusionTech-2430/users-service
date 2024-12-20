package co.allconnected.fussiontech.usersservice.services;

import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseService {

    public String uploadImg(String imageName, String extension, MultipartFile imageFile) throws IOException {
        InputStream inputStream = imageFile.getInputStream();
        Bucket bucket = StorageClient.getInstance().bucket();
        bucket.create("user_photos/"+imageName, inputStream, "image/"+extension);
        return bucket.get("user_photos/"+imageName)
                .signUrl(360, java.util.concurrent.TimeUnit.DAYS).toString();
    }

    public void deleteImg(String imageName) {
        Bucket bucket = StorageClient.getInstance().bucket();
        bucket.get("user_photos/"+imageName).delete();
    }

    public String createUser(String email, String password, String[] roles) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        // Add custom claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        // Create user and set custom claims
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        FirebaseAuth.getInstance().setCustomUserClaims(userRecord.getUid(), claims);
        return userRecord.getUid();
    }

    public void deleteUser(String uid) throws FirebaseAuthException {
        FirebaseAuth.getInstance().deleteUser(uid);
    }

    public void disableUser(String uid) throws FirebaseAuthException {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                .setDisabled(true);
        FirebaseAuth.getInstance().updateUser(request);
    }

    public void enableUser(String uid) throws FirebaseAuthException {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                .setDisabled(false);
        FirebaseAuth.getInstance().updateUser(request);
    }

    public void updateUser(String uid, String email, String password) throws FirebaseAuthException {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid);
        if (email != null)
            request.setEmail(email);
        if (password != null)
            request.setPassword(password);
        FirebaseAuth.getInstance().updateUser(request);
    }

    public void updateCustomClaims(String uid, String[] roles) throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);    }
}
