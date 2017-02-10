# ProgressNevigationBar

[![ProgressBar](https://gifs.com/gif/j2VxVW)

It is recomended to use NavigationBar in Horizontal ScrollView

		<com.library.NavigationBar
			android:id="@+id/navBar"
			android:layout_width="match_parent"
			android:layout_height="56dp"
			android:background="#ffffffff"
			android:padding="6dp"
			app:central_line_height="1dp"
			app:enable_border="true"
			app:only_border="false"
			app:tab_border_color_array="@array/border_state_colors"
			app:tab_color_array="@array/indicator_colors"
			app:tab_indicator_color="@color/darkYellow"
			app:tab_padding="48dp"
			app:tab_strok_width="3dp"
			app:tab_text_color_array="@array/text_state_colors"
			app:tab_text_size="18sp"/>

There is an array.xml which is having tab_border_color_array, tab_color_array and tab_text_color_array
You can refer this file [array.xml](https://github.com/ashutiwari4/ProgressNevigationBar/blob/master/app/src/main/res/values/array.xml)

Implement these Two Interfaces

	NavigationBar.OnTabSelected, NavigationBar.OnTabClick

And Below is code to setup the NavigationBar

     	NavigationBar bar = (NavigationBar) findViewById(R.id.navBar);
        bar.setOnTabSelected(this);
        bar.setOnTabClick(this);
	
		bar.setTabCount(count);
        bar.animateView(3000);
        bar.setCurrentPosition(pos);

To reset nevigationBar 

	bar.resetItems();

