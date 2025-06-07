# Run Tracker

Run Tracker is a multi-module running tracker app for phones.

![Run Feature](https://pl-coding.com/wp-content/uploads/2024/04/run-feature.png)
<table>
  <tr>
    <td>
      <img src="https://pl-coding.com/wp-content/uploads/2024/04/auth-feature.png" alt="Auth Feature" width="500"/>
    </td>
  </tr>
</table>

## Tech Used?

- Multi-module architecture
- Gradle for large-scale projects (version catalogs & convention plugins)
- Authentication systems (OAuth / token refresh)
- Offline-first development
- Dynamic feature modules
- Google Maps SDK
- Jetpack Compose in multi-module projects

## How do you run the project?

In order to run the project on your phone, you'll need to first clone it and then add two API keys for:
1. ... the Run Tracker API
2. ... Google Maps (needs to be got from Google Cloud Console - instructions in the course)

Then simply include them in `local.properties`:
```
API_KEY=<RUN_TRACKER_API_KEY>
MAPS_API_KEY=<GOOGLE_MAPS_API_KEY>
```
Afterwards, build the project and you're ready to use it.
