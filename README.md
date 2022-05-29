# pluvia
Pluvia, a computer game for rainy days

# Controls
| Control |Purpose      |
|----------|:-------------                                                                       |
|Space|drops more stones into the level|
|Left mouse button|clicking a stone with the left mouse button will push the stone to the left    |
|Right mouse button|clicking a stone with the righjt mouse button will push the stone to the right|
|ESC|open Pause Dialog                                                                            |
|ESC|open Pause Dialog                                                                            |
|W/cursor up|move camera towards the screen (only available in debug mode)|
|A/cursor left|move camera left (only available in debug mode)|
|D/cursor right|move camera right (only available in debug mode)|
|S/cursor down|move camera away from the screen (only available in debug mode)|
|Print|save screenshot of various fbos into the screenshot folder (only available in debug mode)|
|I|open info pane including debug inforamtion (only available in debug mode)|
|F5|toggle debug mode (only available in debug mode)|
|F6|show time graphs (only available in debug mode)|
|Middle mouse button|clicking and move mouse to tilt camera (only available in debug mode)|


# Configuration
Pluvia can be configured usign the config/pluvia.propertries file.

| Property |      Default      |  Example | Description |
|----------|:-------------: |------:                  |--------                          |
|pluvia.foregroundFPS |60   |pluvia.foregroundFPS=160 |frames per second to try to match |
|pluvia.monitor       |0    |pluvia.monitor=1         |monitor to use, if more than one are connected, 0 is promary |
|pluvia.vsync         |true |pluvia.vsync=false       |virtual syn with monitor refresh rate |
|pluvia.debugMode     |false|pluvia.debugMode=true    |debug mode allows to pan the camera and see various fbos| 
|pluvia.shadowMapSize |4096 |pluvia.shadowMapSize=8192|the bigger, the better, but you need enough video card ram|
|pluvia.showFps       |false|pluvia.showFps=true      |display frames per second in lower left corner|
|pluvia.showGraphs    |false|pluvia.showGraphs=true   |cpu/gpu graphs can be displayed using F6|
|pluvia.fullscreenMode|true|pluvia.fullscreenMode=false|window mode or full screen mode|

# How To Report a Bug
Please contact me to report a bug or just create a ticket at https://github.com/kunterbunt2/pluvia/issues  
A direct x diagnostic report is very helpfull, as every system is different.

# How to Genrate a DirectX Diagnostic Report
1. Open DirectX Diagnostic Tool by clicking the Start button Picture of the Start button, typing dxdiag in the Search box, and then pressing ENTER.
2. A window will pop up asking if you wish to check if your drivers are digitally signed.
3. Select No
4. On the next window, select Save All Information
5. Save the file to your desktop
6. attach the dxdiag.txt file to teh ticket

# Tested on Following Systems

## Test System 1
* Windows 11 Home 64-bit
* AMD Ryzen 9 3950X 16-Core Processor
* 64GB RAM
* NVIDIA GeForce RTX 2080 Ti
* Feature Levels: 12_2,12_1,12_0,11_1,11_0,10_1,10_0,9_3,9_2,9_1
* Display 2560 x 1440

## Test System 2
* Windows 11 Home 64-bit
* AMD Ryzen 9 3950X 16-Core Processor
* 16GB RAM
* VMware Virtual SVGA 3D Graphics Adapter
* Feature Levels: 10_0,9_3,9_2,9_1
* Display 2560 x 1440
