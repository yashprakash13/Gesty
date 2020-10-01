# Gesty
![Something](https://github.com/yashprakash13/Gesty/blob/master/logo.jpg "Gesty - The Reader")

A basic EBook Reader app with hands off reading feature - use gestures to control the page turn and use automatic scrolling for pages with adjustable speed of scroll.

## Features:
* Functions as a standard Epub Book Reader
* With full screen support for reading
* Home screen shows all recently read books

## Special Feature - The Gesty Mode
* Hands off Reading feature
* A wave of hand in front of the front camera can turn pages
* The gesture supports - 
  * Next page turn
  * Previous page turn
  * Enabling and disabling the automatic scroll mode for reading
* The Auto Scroll mode is enabled with the Gesty Mode
  * It supports enabling and disabling by hand gesture, and
  * Altering the speed of scroll for reading
 
## Package details
* `utils` folder containing:
  - file of application wide constants in use
  - Swipe Detector using the Proximity sensor of the phone to detect hand swipes
* `mainFragments` directory with :
  - model class for each book 
  - UI screen, adapters for displaying book covers in horizontal recyclerview
* `library` folder containing the java class which uses async to add a new book to the library
* `gestyMode` which defines the different kinds of swipe gestures user can apply for page turning(next and previous) and for adjusting the page scrolling speed. 
* `settings` for app wide preferences - stores user choice of how fast they can read - thus adjusting speed of scroll based on that.

## Download
[Download the app from here](https://github.com/yashprakash13/Gesty/releases/download/v1.0/app-release.apk)

## Screens
![1](https://github.com/yashprakash13/Gesty/blob/master/Screenshots/1.png) ![2](https://github.com/yashprakash13/Gesty/blob/master/Screenshots/2.png)
![3](https://github.com/yashprakash13/Gesty/blob/master/Screenshots/3.png)
![4](https://github.com/yashprakash13/Gesty/blob/master/Screenshots/4.png) ![5](https://github.com/yashprakash13/Gesty/blob/master/Screenshots/5.png)

## Gesty Mode in action!
![Gesty-Mode GIF](https://github.com/yashprakash13/Gesty/blob/master/gestymode.gif)

## How to use the Gesty Mode
> * Enable Gesty Mode from the Reading action bar.
> * The Scrolling menu appears - Select the scrolling speed you like.
> * The scroll starts; to turn to the next page, simply hover your hand over the front camera and take it off immediately.
> * To turn to the previous page, hover your hand for two seconds and then take it off.
> * To stop the scrolling or to start it back on, hover your hand for three or more seconds over the front camera.

## TODO
This project can have many extra features for becoming a good ebook reader app:
* Develop the *My library* fragment
* Add other reading customization features, like a dark mode.

## Contributions
Contributions to this app are very welcome. Send a pull request to show what work you've done in improving this project.

## Thank you so much
* DiscreteScrollView   https://github.com/yarolegovich/DiscreteScrollView
* RuntimePermission    https://github.com/florent37/RuntimePermission
* FilePicker           https://github.com/Angads25/android-filepicker

## License
BSD 3-Clause License

Copyright (c) 2019, Yash Prakash
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
