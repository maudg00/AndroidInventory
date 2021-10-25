package com.example.posesionista
// ArticleTableViewModel.kt
//
//  The ArticleTableViewModel class is our application's view model.
//  It will keep our article inventory, and have methods associated to it.
//  Created by Mauricio de Garay Hernández on 24/10/2021.
//
//

//Dependencies
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.random.Random

class ArticleTableViewModel:ViewModel() {
    /* Attribute Prototypes. */
    //The inventory is our article inventory list. We want to keep track of it
    val inventory= mutableListOf<Article>()
    //Array of random article names for testing purposes only. If articles are added manually, it will not be needed.
    private val names=arrayOf("Phone", "Bread", "Shirt")
    //Array of random article adjectives for testing purposes only. If articles are added manually, it will not be needed.
    private val adjectives=arrayOf("Expensive", "Grey", "Warm")
    /* Method Prototypes. */

    /*
    * The init method is the one responsible for immediately initializing a random article list with the previously defined names and adjectives.
    * For testing purposes only. If articles are added manually, it will not be needed.
    *
    * @params
    *
    *
    * @returns
    *
    *
    */
    init {
        //Adding 100 random articles to list.
        for(i in 0 until 100)
        {
            val article=Article()
            //Random name, adjectives and price.
            val randomName=names.random()
            val randomAdjective=adjectives.random()
            val randomPrice= Random.nextInt(1000)
            article.name="$randomName $randomAdjective"
            article.cost=randomPrice
            //Random serial number is a simple hexadecimal name hashcode plus a random number. For testing purposes only.
            article.serialNumber=Integer.toHexString(article.name.hashCode()+randomPrice)
            //Add random article to list.
            inventory+=article
        }
    }
    /*
    * The addArticle method is the one responsible for adding a received article to the inventory list.
    *
    *
    * @params
    *   article Article: the article to be added to the inventory
    *
    * @returns
    *
    *
    */
    fun addArticle(article: Article)
    {
        inventory.add(article)
    }
    /*
    * The swapPositions method is the one responsible for swapping the position of 2 items in our inventory
    *
    *
    * @params
    *   startPosition Int: the position of our first element.
    *   endPosition Int: the position of our second element.
    *
    * @returns
    *
    *
    */
    fun swapPositions(startPosition:Int, endPosition:Int)
    {
        Collections.swap(this.inventory, startPosition, endPosition)
    }
    /*
    * The deleteFromInventory method is the one responsible for deleting an article from our inventory.
    *
    *
    * @params
    *   position Int: the position of the element we wish to remove from the inventory.
    *
    * @returns
    *
    *
    */
    fun deleteFromInventory(position:Int)
    {
        inventory.removeAt(position)
    }
}