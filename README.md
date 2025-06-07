# Run Tracker

Run Tracker is a multi-module running tracker app for phones.



https://github.com/user-attachments/assets/ff49986d-a036-4551-b8d7-66eab98d8166



## Tech Used?

- Multi-module architecture
- MVI + Clean Architecture
- Version catalogs & convention plugins
- Authentication systems (OAuth / token refresh)
- Offline-first development
- Google Maps SDK
- Jetpack Compose with multi-modules

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
