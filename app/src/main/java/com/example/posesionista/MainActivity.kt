// MainActivity.kt
//
//  The main activity class works as a controller for the entire application, communicating with both the view and the model.
//  It handles the instantiation of fragments, as well as back press button validations.
//  Created by Mauricio de Garay Hernández on 24/10/2021.
//
//


package com.example.posesionista

//Dependencies
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


class MainActivity : AppCompatActivity(), ArticleTableFragment.ArticleTableInterface {
    /* Attribute Prototypes. */
    //The ArticleFragment instance, we want to save this to validate back press activities that take place in this fragment.
    private var myArticleFragment:ArticleFragment?=null
    /* Method Prototypes. */

    /*
    * The method onCreate is the method that will be executed whenever the create lifecycle event happens.
    * This method starts the first fragment: ArticleTableFragment. This allows the program to begin properly
    *
    * @params
    *   @param savedInstanceState Bundle?: the savedInstanceState.
    *
    * @returns
    *
    *
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Lets get our current fragment
        val currentFragment=supportFragmentManager.findFragmentById(R.id.fragment_container)
        //Add the ArticleTableFragment here, to start the application properly.
        if(currentFragment==null)
        {
            val newFragment=ArticleTableFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, newFragment).commit()
        }
    }
    /*
    * The method onStart is the method that will be executed whenever the start lifecycle event happens.
    * We want to override this method to keep the ArticleFragment running instance.
    * This will allow the back press validations work despite a screen orientation change.
    *
    * @params
    *
    *
    * @returns
    *
    *
    */
    override fun onStart() {
        super.onStart()
        //Lets check if an ArticleFragment instance is running
        if(supportFragmentManager.findFragmentByTag("ArticleFragment")!=null)
        {
            //If it is, lets save it to our attribute to continue working on the same article
            myArticleFragment=supportFragmentManager.findFragmentByTag("ArticleFragment") as ArticleFragment
        }
    }
    /*
    * The method onBackPressed is the method that will be executed whenever the back press button gets pressed.
    * This will allow us to validate the actions we want on a back press.
    * For example: here if we are on article fragment, we do not want to go back if either the name or serial number are empty.
    *
    * @params
    *
    *
    * @returns
    *
    *
    */
    override fun onBackPressed() {
        if(myArticleFragment==null) return
        //If there is an article fragment instance and there's a name and serial number, let's go back to the article table.
        if(myArticleFragment!!.allowBackPress) super.onBackPressed()
        else{
            //Else, let the user know that they must enter name and serial number.
            Toast.makeText(applicationContext, "Error: Name and Serial Field cannot be empty.", Toast.LENGTH_SHORT).show()
        }

    }
    /*
    * The method onArticleClicked is the method that will be executed whenever an article from the article table gets clicked
    * This will allow us to instantiate an article fragment with the selected article
    *
    * @params
    *   article Article: the selected article object.
    *
    * @returns
    *
    *
    */
    override fun onArticleClicked(article: Article) {
        //Lets start the fragment, with the tag "ArticleFragment" so we can monitor it in the lifecycle events. The sent article will be the one in the fragment.
        myArticleFragment=ArticleFragment.newInstance(article)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, myArticleFragment!!, "ArticleFragment").addToBackStack(null).commit()
    }


}