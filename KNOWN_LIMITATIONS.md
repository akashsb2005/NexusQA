\# Known Limitations \& Future Work



\- Selenium Grid (Milestone 12) is configured in code but not currently verified running locally due to a Docker Desktop environment issue on the dev machine; CI does not currently use the Grid path (runs local headless Chrome directly on the runner).

\- Edge browser is not supported in Grid mode (official Selenium Grid images only ship Chrome/Firefox nodes).

\- Retry mechanism uses a fixed max of 2 retries; not currently configurable via config.properties.

\- No parallel test execution configured yet — tests run sequentially.

\- CI Allure report is uploaded as a build artifact but not published to a persistent site (e.g., GitHub Pages) yet.

