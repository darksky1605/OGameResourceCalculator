# OGameResourceCalculator

This is a tool for the browser game OGame. 

It calculates how many days you need to produce resources to met specific resource requirements.
Resource trades are taken into account. You can specify individual exchange rates for each resource.

How to build: 
Use ANT to run the build script

How to run:
java -jar ogameresourcecalculator.jar

Possible arguments:
--lang=XXX  , where XXX specifies the language. 
Valid options for XXX: de, en. Default : de

Example: Start english version
java -jar ogameresourcecalculator.jar --lang=en

How to use:
Enter daily production
Enter resources you already have (optional)
Enter resources you want to have
Press Go

The current exchange rates and daily productions can be saved using the buttons in the upper right corner.
Doubleclick on a profile sets the input fields to the profile data.

If checkbox "advanced trade" is checked, trades using intermediate resources are used if you get more resources than with an ordinary trade.
For example, without checkbox the suggest trade may be metal -> deuterium, but with checkbox the suggest trade could be metal -> crystal -> deuterium.

The default resolution of 1 should be fine, but if you want to know that you need 9.5 or 9.45 days instead of 10 days, you can change it. Minimum resolution 0.01 .
