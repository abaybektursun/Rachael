# Rachael

#### Application Client Steps

1. Application start
  + Runs face detection service
  + Establishes connection to server
2. Face detected in front of the web camera
  + Images of the face are communicated to the server
    - If multiple face detected closest one will be selected
  + Information about the user retrieved in case face was recognized by the server
  + New user registration if user is not found
3. User selects and calls a contact
  + Contact on the other side will immediately see the video stream from callers webcam
  + If contact accpets the call, video chat will begin

> Right now application uses P2P so, server is not requrired. Just a detected face will log you in with default properties

#### TODO:
- [ ] Create a proper build for the client
- [ ] Fix the server OpenCV issue
- [ ] Port the server to Windows
- [ ] Add ability to add new contacts
- [ ] Create pure JavaFX animation rather than CSS or GIF
- [ ] Complete unit testing