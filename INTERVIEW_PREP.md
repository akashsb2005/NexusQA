\# NexusQA — Interview Prep Sheet



\## Architecture \& Design

\- Walk me through the overall structure of this framework (Page Object Model, config, listeners, DB/API layers).

\- Why Page Object Model instead of writing raw Selenium calls in test methods?

\- How does DriverFactory manage thread safety, and why does that matter?



\## Retry \& Resilience (Milestone 11)

\- Why is unlimited retry an anti-pattern?

\- Explain the fallback-locator pattern and how it differs from true AI self-healing locators.



\## CI/CD (Milestone 13)

\- Walk through what happens from `git push` to a green checkmark.

\- Why does the CI runner need headless mode when local doesn't require it?

\- How is the Postgres dependency handled differently in CI vs. locally?



\## Reporting (Milestone 14)

\- Explain Allure's two-step architecture (result collection vs. report generation).

\- How does a screenshot get attached to a failed test automatically?



\## Docker / Grid (Milestone 12)

\- What problem does Hub/Node architecture solve?

\- What went wrong in your own environment, and how did you diagnose it? (This is a genuinely good story — real troubleshooting under a cascading failure is a strong interview answer if told honestly and concisely.)



\## General

\- What would you improve if you had another two weeks on this project?

\- What was the hardest bug to track down in this whole project, and how did you find the root cause?

