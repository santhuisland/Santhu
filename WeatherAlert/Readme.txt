##### SDK Versions
minSdkVersion = 19
targetSdkVersion = 19


##### Application Packages
1)AppManager - contains API Management with data structures
2)AsyncCommServer - contains Server Request/Response Handlers with Transport modules
3)UI - contain MainActivity
4)Common - Globals,commons,progressbar,utility,debuglog,preference manager
5)datamodels - contains data structure  classess with parsing the JSON response


###### Application Unit TestCase
1)ApplicationTest  - contains Unit testcase to test Server API to download weather report


###### Librarys 
1)'com.android.support:appcompat-v7:23.1.0'
	 'com.android.support:design:23.1.0'    - for UI
2)'com.google.guava:guava:18.0' - 	used  utility methods for working with byte arrays and I/O streams
3)'junit:junit:4.12' - used for UnitTest framework


##### Application Functionality
1)User can Add new city and code to Fetch Weather report from http://openweathermap.org/
2)User can see last saved Favourate location on App launch time
3)User can see the Favourate list with list of all added Favourate citites
4)User can delete the city from Favourate location by Long pressing the list item
5)On Main screen User can see the Weather Report with Location and wind info(Speed,Degree) with "Cardinal direction" image 
	with arrow showing the wind direction in degrees.
6)Also toolbar contain refresh button to refresh the current city.
