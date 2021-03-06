MP3TagDroid
===========

Android app to view/edit audio file tags.

Do you get annoyed by inaccurate/incorrect tags in your music collection? MP3TagDroid can help you fix that! Just load your audio file, make corrections and touch Save.

MP3TagDroid tries to re-use an existing file manager to create a file browser dialog. I did not want to re-invent another file browser. So, you may have to install a file manager app to use MP3TagDroid. I use [X-plore](http://www.lonelycatgames.com/?app=xplore), but any other file manager that handles [ACTION_GET_CONTENT](http://developer.android.com/reference/android/content/Intent.html#ACTION_GET_CONTENT) should be okay.

Disclaimer: This software is provided as is, no warranties please :)

Screenshots
-----------
<img alt="Screenshot without audio file" src="https://github.com/gowthamgowtham/MP3TagDroid/blob/master/MP3TagDroid/screens/main_screen_no_audio_file.png?raw=true" style="padding: 5px 5px 5px 5px;"/>
<img alt="Screenshot with audio file" src="https://github.com/gowthamgowtham/MP3TagDroid/blob/master/MP3TagDroid/screens/main_screen_with_audio_file.png?raw=true" style="padding: 5px 5px 5px 5px;"/>

Current Status
--------------

Alpha quality. It seems to work on my Samsung Galaxy S7562.

Version History
---------------

0.1 - Initial alpha quality version

TODO
----

 * Allow editing multiple audio files at once
 * Allow adding new album art image
 * Add more error checking

Credits
-------

All the heavy lifting is done by [myid3](http://www.fightingquaker.com/myid3) library. This is a free java ID3 tag library.


