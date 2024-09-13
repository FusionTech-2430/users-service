package co.allconnected.fussiontech.usersservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private final FirebaseConfigProperties firebaseConfigProperties;

    public FirebaseConfig(FirebaseConfigProperties firebaseConfigProperties) {
        this.firebaseConfigProperties = firebaseConfigProperties;
    }

    @PostConstruct
    public FirebaseApp initializeFirebase() throws IOException {
        firebaseConfigProperties.setPrivate_key(
                firebaseConfigProperties.getPrivate_key().replace("\\n", "\n")
        );

        String json = new Gson().toJson(firebaseConfigProperties);

        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(json.getBytes()));

         FirebaseOptions options = FirebaseOptions.builder()
                 .setCredentials(credentials)
                 .setStorageBucket("allconnected-4855c.appspot.com")
                 .build();

        if(FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirebaseApp.getInstance();
    }
}
