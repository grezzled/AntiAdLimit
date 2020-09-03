[![](https://jitpack.io/v/grezzled/AntiAdLimit.svg)](https://jitpack.io/#grezzled/AntiAdLimit)
# Getting Started
To get a Git project into your build:

**Step 1.** Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

    
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
**Step 2.** Add the dependency

    dependencies {
	        implementation 'com.github.grezzled:AntiAdLimit:1.0-beta'
	}

## Basic Usage
**Step 1.** Create and host a JSON file on your Server.
Please make sure to have a direct link to the hosted JSON file (e.g: https://mysite.com/file.json ) 

**Step 2.** Edit the JSON file content

      {
          "networks": {  
            "admob_activated": true,  
            "fan_activated": false  
          },  
          
          "ad_units": [  
            {  
              "limit_activated": true,  
              "unit_id": "1564516670394005_1564551527057186",  
              "ads_activated": true,  
              "clicks": 2,  
              "impressions": 20,  
              "delay_ms": 3000,  
              "ban_hours": 1,  
              "hide_on_click": true  
            },  
            {  
              "limit_activated": true,  
              "unit_id": "1564516670394005_1564522647060074",  
              "ads_activated": true,  
              "clicks": 2,  
              "impressions": 20,  
              "delay_ms": 3000,  
              "ban_hours": 1,  
              "hide_on_click": true  
             }
          ]  
        }
        
**Networks Params Explained**
| Param |  Description| Type | 
|--|--|--|
| admob_activated | Activate Admob units |  [`boolean`] Accepts **true** or **false**|
| fan_activated | Override Admob units and use Audience Network units.| [`boolean`] Accepts **true** or **false**|
***Please notice  1:*** *if they are both **false** no Ad units will display so no Ads will run.*
***Please notice  2:*** *if they are both **true** Audience network will be used*

**AdUnits Params Explained**
| Param |  Description| Type | 
|--|--|--|
| limit_activated| Weither to activate limitation for this unit. | [`boolean`] Accepts **true** or **false**|
| unit_id | This is where you set the AD Unit Id (Either Audience Network or Admob) | [`string`] Accepts a string value between double quotes ""| 
|ads_activated| Enable or Disable unit, if it's `fasle` this unit will not show any ads  | [`boolean`] Accepts **true** or **false**|

.. In Progress




