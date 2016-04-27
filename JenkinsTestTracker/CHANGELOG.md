# Changelog

### Latest Version

Looked at issue #110 to find the cause of the memory leak and the System Digest table appears to be fine and the immediate issue seems to be multiple digest viewers being created when only one should be. This has been addressed so that there is only one digest. There should be no difference from the users perspective other than a memory improvement. 

###1.3.70 (Apr 27, 2016 3:09:09 PM)

Attempted to resolve issue specifically noticed on Windows, issue #95, where the split pane divider position is lost. It's not completely clear where the issue is, and having researched how the split pane works it seems to have some quirks. The issue found on Mac, with another larger screen, was going from maximised to not maximised back to maximised was resetting the divider location to the middle (ish). While this may not be the exact issue on Windows, it is definitely an issue for Mac. This has been fixed.

###1.3.69 (Apr 27, 2016 10:02:36 AM)

Refactored test for job importing to have more flexible test cases, mocking the structure to parse. Consequently, the careless exception catching has been removed since the parsing should now be correctly defended. No functionality/behaviour changes expected.

###1.3.68 (Apr 24, 2016 9:38:41 AM)

Refactored models updates when jobs and details for jobs are imported. This will allow more complicated logic to be performed on imports in future if needed. Also resolved an issue where failures were being cleared by certain types of Jenkins Jobs (such as Maven).

###1.3.67 (Apr 22, 2016 7:43:33 PM)

Refactored json user importer to get rid of the try catches that were unnecessary.

###1.3.66 (Apr 22, 2016 12:28:35 PM)

Working on issue 83. Refactored the user import test and extracted common object for converting from string response from Jenkins to JSONObject. This allows the logic to be simplifed and testing to be simplified elsewhere.

###1.3.65 (Apr 22, 2016 7:54:35 AM)

Internal changes for handling conversion from jenkins api response to JSON import. Not integrated yet.

###1.3.63 - 1.3.64 Unreleased

###1.3.62 (Apr 21, 2016 12:23:23 PM)

Solved Issue 106:

Wrapped build wall configuration panels in scroll panes so that when there are lots of jobs, they can be reached and configured.

###1.3.61 (Apr 21, 2016 7:41:09 AM)

Internal changes looking at issue 93 to try and simplify constructor of dual buil wall. Factored out separate clear methods and tested interaction with configuration window controller.

###1.3.60 (Apr 20, 2016 9:33:20 PM)

First published changelog.

###1.3.59 (Apr 20, 2016 12:58:18 PM)

Solved Issue 99:

The dual wall now provides an option on its context menu to "Open Configuration Window". This will open a new window with a configuration panel for each of the walls, and a configuration panel for the image flasher (left, image flasher, right). This window can be used to configure the build wall in the same way as on the build wall itself. 

The use case for this is having the build wall on a separate monitor (which maybe isn't so easy to see, or is far away) but needing to configure the wall from another monitor.

The window can be closed/hidden and reopened from the context menu.

###1.3.58 (Apr 19, 2016 8:35:31 PM)

Solved Issue 41:

The build wall configuration panel now provides a set all option allowing the user to set the policy for each job to a specific value. This helps when handling large numbers of jobs where the majority may be ignored or use a specific rule. 

To set all the user simply selects the policy from the drop down at the top of the policies section, and then clicks the set all button. 

###1.3.57 (Apr 19, 2016 12:49:11 PM)

Solved Issue 98:

It was observed that when a new build appears it was being shown as a failure which is incorrect because there is no initial state, in fact the job is NOT_BUILT. This was thought to be a colouring issue but actually the initial status of a job was failure. This has been corrected to not built.

###1.3.56 (Apr 19, 2016 12:29:03 PM)

Solved Issue 91:

Added a simple description for aborted and failed builds in place of the test results. When a job is aborted the panel suggests it may be related to a manual command or a timeout in the build. Similarly, failures report that it may be compilation or partial commit issues.

###1.3.55 (Apr 19, 2016 12:06:40 PM)

Solved Issue 102:

The failing tests associated with unstable jobs were not being removed when the job then passed, or changed state. This had the effect that failures may show up at a later point on the build when it was not unstable. This has been fixed so that they are added when a job has been built and is unstable and removed when a job changes to any state from unstable.

###1.3.54 (Apr 19, 2016 11:49:17 AM)

Internal changes to tidy up tests.

###1.3.53 (Apr 19, 2016 7:37:54 AM)

Solved Issue 55:

Test results are now imported when a job becomes unstable. The efficiency added in to stop tests being imported has been removed completely and the conditions for updating tests added. This now means that the test table (which needs updating) will only have tests from failing builds.

###1.3.52 (Apr 19, 2016 6:41:30 AM)

Internal changes to parsing test results for a specific job that will associate the test cases failing (or regression) with the job the test results are for. 

###1.3.51 (Apr 18, 2016 7:36:17 PM)

Internal changes to refactor test and warnings removal.

###Before Changelog History

The process for developing this project is to work towards milestones where each milestone has a release with documentation on the wiki pages. For more information, this changelog fills in the day-to-day commits and minor releases published using a continuous delivery model.

There is a lot of information available on the GitHub project page (https://github.com/DanGrew/JenkinsTestTracker). Looking back through commits and issues will give you more information if needed about what is included. Of course, email me if you want any other information.


