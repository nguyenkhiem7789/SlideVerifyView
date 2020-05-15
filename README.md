# SlideVerifyView

https://www.youtube.com/watch?v=-5KgXRADUMo

<img src="https://user-images.githubusercontent.com/18132015/82021741-974eeb00-96b5-11ea-9256-181d9f8c2fee.jpg" width="300"/>

# How to

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

    gradle
    maven
    sbt
    leiningen

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.nguyenkhiem7789:SlideVerifyView:0.1.0'
	}

# How to use it?

In XML: 

    <com.nguyen.verifyview.VerifyView
        android:id="@+id/verifyView"
        android:layout_width="match_parent"
        android:layout_height="44dp"
        android:layout_margin="16dp"
        app:verify_bg_color="@color/colorAccent"
        app:verify_stroke_color="@color/colorAccent"
        app:verify_stroke_width="1dp"
        app:verify_text="Truợt sang phải để thao tác"
        app:verify_progress_color="@color/colorPrimary"
        app:verify_radius_image_thumb="8dp"
        />
        
       
 In Java, you can listener when checked finish: 
 
        verifyView.setVerifyCheckedListener(object : VerifyCheckedListener {
            override fun onChecked() {
                Toast.makeText(applicationContext, "Checked", Toast.LENGTH_SHORT).show()
            }
        })
