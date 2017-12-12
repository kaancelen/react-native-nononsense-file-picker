# react-native-nononsense-file-picker

A React Native Android module that allows you to use custom UI to select a file from the device.
It is based on [NoNonsense-FilePicker](https://github.com/spacecowboy/NoNonsense-FilePicker)

## Usage
```javascript
import FilePicker from 'react-native-nononsense-file-picker';
...
//If extensions is empty or null or undefined then FilePicker doesn't apply any filtering 
const extensions = ['.png', '.jpg', '.jpeg', '.doc', '.docx', '.xls', 'xlsx', '.pdf'];
const res = await FilePicker.pick({type: extensions});
console.log(res);
//res has 2 attribute, path and uri
//{path: 'path of file', uri: 'uri of file'}
```

### Installation
`npm install react-native-nononsense-file-picker --save`
### React package configuration
```gradle
// file: android/settings.gradle
...

include ':react-native-nononsense-file-picker'
project(':react-native-nononsense-file-picker').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-nononsense-file-picker/android')
```
```gradle
// file: android/app/build.gradle
...
dependencies {
    ...
    compile project(':react-native-nononsense-file-picker')
}
```
```java
// file: MainApplication.java
...
import com.kaancelen.rn.nononsense.filepicker.RNNFPPackage; // import package

public class MainApplication extends Application implements ReactApplication {

   /**
   * A list of packages used by the app. If the app uses additional views
   * or modules besides the default ones, add more packages here.
   */
    @Override
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new RNNFPPackage() // Add package
        );
    }
...
}
```
### NoNonsenses File Picker configuration
```xml
<!-- file: android/src/res/values/colors.xml -->
...
    <!-- You can change the colors of picker from here -->
    <color name="filePickerPrimary">#3F51B5</color>
    <color name="filePickerDark">#303F9F</color>
    <color name="filePickerAccent">#FF4081</color>
...
```
```xml
<!-- file: android/src/res/values/styles.xml -->
...
    <style name="FilePickerTheme" parent="NNF_BaseTheme">
        <item name="colorPrimary">@color/filePickerPrimary</item>
        <item name="colorPrimaryDark">@color/filePickerDark</item>
        <item name="colorAccent">@color/filePickerAccent</item>
        <item name="nnf_list_item_divider">?android:attr/listDivider</item>
        <item name="alertDialogTheme">@style/FilePickerAlertDialogTheme</item>
    </style>

    <style name="FilePickerAlertDialogTheme" parent="Theme.AppCompat.Dialog.Alert">
        <item name="colorPrimary">@color/filePickerPrimary</item>
        <item name="colorPrimaryDark">@color/filePickerDark</item>
        <item name="colorAccent">@color/filePickerAccent</item>
    </style>
...
```
```xml
<!-- file: android/src/main/AndroidManifest.xml -->
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.myApp">
...
    <application>
    ...
        <activity
            android:name="com.kaancelen.rn.nononsense.filepicker.FilteredFilePickerActivity"
            android:label="@string/app_name"
            android:theme="@style/FilePickerTheme">
            <intent-filter>
                <action android:name="android.intent.action.GET_CONTENT" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/nnf_provider_paths" />
        </provider>
        ...
    </application>
```