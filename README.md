# Real-Time Chatting Application

This is a real-time chatting application built with Firebase for email authentication and real-time database. The app allows users to sign in with their email, view a list of other users, and start a chat with any user from the list.

## Features

- Email authentication using Firebase
- Real-time user list fetched from Firebase Realtime Database
- Start a chat with any user from the list
- Sign out functionality

## Screens

1. *Login Screen*: Allows users to sign in with their email and password.
2. *User List Screen*: Displays a list of users fetched from Firebase.
3. *Chat Screen*: Allows users to chat in real-time with selected users.

## Setup and Installation

1. *Clone the repository*:
    bash
    git clone https://github.com/yourusername/your-repo-name.git
    cd your-repo-name
    

2. *Open the project in Android Studio*.

3. *Configure Firebase*:
    - Go to the [Firebase Console](https://console.firebase.google.com/).
    - Create a new project or use an existing project.
    - Add an Android app to your Firebase project.
    - Follow the instructions to download the google-services.json file.
    - Place the google-services.json file in the app directory of your Android project.

4. *Add Firebase dependencies*:
    Ensure that your build.gradle files are configured with the necessary Firebase dependencies. For example:

    gradle
    // Project-level build.gradle
    buildscript {
        dependencies {
            classpath 'com.google.gms:google-services:4.3.10'
        }
    }

    // App-level build.gradle
    apply plugin: 'com.google.gms.google-services'

    dependencies {
        implementation 'com.google.firebase:firebase-auth:21.0.1'
        implementation 'com.google.firebase:firebase-database:20.0.3'
    }
    

5. *Run the app*:
    - Connect your Android device or start an emulator.
    - Click on the "Run" button in Android Studio.

## Usage

1. *Sign In*:
    - Open the app and sign in with your email and password.
    - If you don't have an account, create one using the sign-up option.

2. *View User List*:
    - After signing in, you will see a list of users fetched from Firebase.

3. *Start a Chat*:
    - Click on any user from the list to start a chat with them.

4. *Sign Out*:
    - Click on the "Sign Out" button to sign out from the app.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any improvements or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
