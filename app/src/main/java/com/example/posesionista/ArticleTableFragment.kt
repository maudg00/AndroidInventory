package com.example.posesionista
// ArticleTableFragment.kt
//
//  The ArticleTableFragment class is the one responsible for handling our article table recycler view and its corresponding methods
//  Created by Mauricio de Garay Hernández on 24/10/2021.
//
//

//Dependencies
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG="ArticleTableFragment"
class ArticleTableFragment: Fragment() {
    /* Attribute Prototypes. */
    //The articleRecyclerView is our table's recycler view.
    private lateinit var articleRecyclerView: RecyclerView
    //The adapter is our article adapter.
    private var adapter: ArticleAdapter?=null
    //myInterface will refer to the object which implements our soon to be defined ArticleTableInterface.
    //In this case, it will be MainActivity.
    private var myInterface:ArticleTableInterface?=null
    //movementManager is an object of our soon to be defined inner class, ListMovementManager.
    //This class contains all the methods to handle dragging and swiping list movements.
    private lateinit var movementManager:ListMovementManager
    //Our articleViewModel is an instance of our ArticleTableViewModel class.
    //This will allow us to keep track of our inventory, access its methods and change anything if needed.
    private val articleViewModel:ArticleTableViewModel by lazy {
        ViewModelProvider(this).get(ArticleTableViewModel::class.java)
    }
    /* Method Prototypes. */

    /*
    * The method updateUI is the method that will call the recycler view to update the recyclerView whenever there's changes on the inventory.
    *
    * @params
    *
    *
    * @returns
    *
    *
    */
    private fun updateUI()
    {
        val inventory=articleViewModel.inventory
        adapter=ArticleAdapter(inventory)
        articleRecyclerView.adapter=adapter
    }
    /*
    * The method onCreate is the method that will be called in the onCreate lifecycle event.
    * This will allow us to successfully tell our application that the fragment has an options menu.
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
        Log.d(TAG, "Size: ${articleViewModel.inventory.size}")
        setHasOptionsMenu(true)
    }
    /*
    * The method onAttach is the method that will be called in the onAttach event.
    * This will allow us to get associate the myInterface attribute to the context which implements the ArticleTableInterface.
    *
    * @params
    *   @param context Context: our context.
    *
    * @returns
    *
    *
    */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterface=context as ArticleTableInterface
    }
    /*
    * The method onDetach is the method that will be called in the onDetach event.
    * This will allow us to null our myInterface attribute.
    *
    * @params
    *
    *
    * @returns
    *
    *
    */
    override fun onDetach() {
        super.onDetach()
        myInterface=null
    }
    /*
    * The method onCreateView will be called during the onCreateView event.
    * This will allow us to initialize our view-related attributes.
    *
    * @params
    *   @param inflater LayoutInflater: our layoutInflater.
    *   @param container ViewGroup: our container.
    *   @param savedInstanceState Bundle?: the savedInstanceState Bundle.
    *
    * @returns
    *   View: the inflated View.
    *
    */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate this fragment
        val myView=inflater.inflate(R.layout.article_list_fragment, container, false)
        //Get our view items and associate them to the attributes.
        articleRecyclerView=myView.findViewById(R.id.article_recycler) as RecyclerView
        articleRecyclerView.layoutManager=LinearLayoutManager(context)
        //Init our movement manager for swipes and drags
        movementManager=ListMovementManager()
        //Attach our movement manager to our recycler view
        ItemTouchHelper(movementManager).attachToRecyclerView(articleRecyclerView)
        //Update UI with our inventory.
        updateUI()
        return myView
    }
    /*
    * The method onCreateOptionsMenu will inflate our options menu.
    *
    *
    * @params
    *   @param menu Menu: our menu.
    *   @param inflater MenuInflater: our MenuInflater.
    *
    * @returns
    *
    *
    */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_article_list, menu)
    }
    /*
    * The method onStart will be called during the onStart event.
    * We will use this to set a manual action bar title for this fragment.
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
        val activityBar=activity as AppCompatActivity
        //Set the action bar title.
        activityBar.supportActionBar?.setTitle(R.string.article_list)
    }
    /*
    * The method onOptionsItemSelected will be called during a menu option click.
    *
    * @params
    *   @param item MenuItem: the selected menu item
    *
    *
    * @returns
    *   Boolean: success of the result.
    *
    */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //If it's the new article option
        return when (item.itemId)
        {
            R.id.newItem->{
                //Add a new article
                val newArticle=Article()
                articleViewModel.addArticle(newArticle)
                //Call the interface so it sends us to article fragment with this newly created article.
                myInterface?.onArticleClicked(newArticle)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }



    }
    /*
    * The method getCellColor will be called whenever we want to set a table field color depending on the article cost.
    *
    * @params
    *   @param price Int: the article cost.
    *
    *
    * @returns
    *   Int: the color code.
    *
    */
    private fun getCellColor(price:Int): Int {
        //Start with white
        var colorCode:Int=ResourcesCompat.getColor(resources, R.color.white, null)
        when (price) {
            //From 0 to 99, return the white color code.
            in 0..99 -> colorCode= ResourcesCompat.getColor(resources, R.color.white, null)
            //From 100 to 199, return the orange color code.
            in 100..199 -> colorCode= ResourcesCompat.getColor(resources, R.color.orange, null)
            //From 200 to 299, return the red color code.
            in 200..299 -> colorCode= ResourcesCompat.getColor(resources, R.color.red, null)
            //From 300 to 399, return the purple color code.
            in 300..399 -> colorCode= ResourcesCompat.getColor(resources, R.color.purple_200, null)
            //From 400 to 499, return the teal color code.
            in 400..499 -> colorCode= ResourcesCompat.getColor(resources, R.color.teal_200, null)
            //From 500 to 599, return the brown color code.
            in 500..599 -> colorCode= ResourcesCompat.getColor(resources, R.color.brown, null)
            //From 600 to 699, return the green color code.
            in 600..699 -> colorCode= ResourcesCompat.getColor(resources, R.color.green, null)
            //From 700 to 799, return the yellow color code.
            in 700..799 -> colorCode= ResourcesCompat.getColor(resources, R.color.yellow, null)
            //From 800 to 899, return the grey color code.
            in 800..899 -> colorCode= ResourcesCompat.getColor(resources, R.color.grey, null)
            //From 900 to 1000, return the light brown color code.
            in 900..1000 -> colorCode= ResourcesCompat.getColor(resources, R.color.light_brown, null)
        }
        //return the color code.
        return colorCode
    }
    /* Interfaces */
    //The interface ArticleTableInterface will allow whichever object implements it, to react to a list article clicked event.
    interface ArticleTableInterface
    {
        //React to the event and article clicked.
        fun onArticleClicked(article: Article)
    }
    /* Inner Classes */
    //The ArticleHolder Class will manage the recycler view holder and its events.
    private inner class ArticleHolder(myView: View): RecyclerView.ViewHolder(myView), View.OnClickListener{
        /* Attribute Prototypes. */
        //The layoutView is our article view
        private val layoutView:ConstraintLayout=itemView.findViewById(R.id.article)
        //The nameTextView is our article's name text view widget.
        private val nameTextView:TextView= itemView.findViewById(R.id.labelName)
        //The costTextView is our article's cost text view widget.
        private val costTextView:TextView= itemView.findViewById(R.id.labelCost)
        //The serialTextView is our article's serial number text view widget.
        private val serialTextView:TextView= itemView.findViewById(R.id.labelSerial)
        //The article attribute is the Article object we are currently working with.
        private lateinit var article:Article
        /* Method Prototypes */
        /*
        * The method binding will be called for item binding.
        *
        * @params
        *   @param article Article: the article object
        *
        *
        * @returns
        *
        */
        @SuppressLint("SetTextI18n")
        fun binding(article:Article)
        {
            //Initialize our article
            this.article=article
            //Initialize our text view fields
            nameTextView.text=article.name
            costTextView.text="$"+article.cost.toString()
            serialTextView.text=article.serialNumber
            //Get color code depending on cost, and change the item layout background color to this color code.
            layoutView.setBackgroundColor(getCellColor(article.cost))
        }
        /*
        * The init getCellColor will allow us to set the onClickListener.
        *
        * @params
        *
        *
        * @returns
        *
        */
        init {
            itemView.setOnClickListener(this)
        }
        /*
        * The method onClick will be called whenever an article gets clicked.
        *
        * @params
        *   @param v View?: the item view.
        *
        *
        * @returns
        *
        */
        override fun onClick(v: View?) {
            //Let the ArticleTableInterface know there was a click event.
            myInterface?.onArticleClicked(this.article)
        }
    }
    //The ArticleAdapter Class will manage the recycler view adapter and its events.
    private inner class ArticleAdapter(var inventory: List<Article>): RecyclerView.Adapter<ArticleHolder>(){
        /* Method Prototypes */
        /*
        * The method onCreateViewGolder will be called when the view holder gets created. This will inflate it.
        *
        * @params
        *   @param parent ViewGroup: the item ViewGroup.
        *   @param viewType int: the ViewType code
        *
        *
        * @returns
        *   ArticleHolder: the inflated article holder
        *
        */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
            //Lets inflate the view holder and return it.
            val holder=layoutInflater.inflate(R.layout.article_layout, parent, false)
            return ArticleHolder(holder)
        }

        /*
        * The method getItemCount will get the inventory's item count.
        *
        * @params
        *
        *
        * @returns
        *   Int: the inventory's item count.
        *
        */
        override fun getItemCount(): Int {
            return inventory.size
        }
        /*
        * The method onBindViewHolder will be called when the view holder bind event happens.
        * This will bind an article to it.
        *
        * @params
        *   @param holder ArticleHolder: our article holder.
        *   @param position int: the article position.
        *
        *
        * @returns
        *
        */
        override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
            val article=inventory[position]
            holder.binding(article)
        }
    }
    //The ListMovementManager Class will list movement events: swipe and drag/move.
    //We only want UP/DOWN for dragging, and only want left for swiping. Thus the parameters in SimpleCallBack.
    private inner class ListMovementManager:ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
        ItemTouchHelper.LEFT

    ){
        /* Method Prototypes */
        /*
        * The method onMove will be called when an item gets moved vertically, for dragging handling.
        *
        * @params
        *   @param recyclerView RecyclerView: the recycler view in which the movement takes place.
        *   @param viewHolder viewHolder: this recycler view's article view holder.
        *   @param target viewHolder: the target position after the movement.
        *
        *
        * @returns
        *   Boolean: operation success.
        *
        */
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            //Get start position on movement with the original holder.
            val start=viewHolder.absoluteAdapterPosition
            //Get target position of movement with the target holder.
            val end=target.absoluteAdapterPosition
            //Move it in the inventory itself.
            articleViewModel.swapPositions(start, end)
            //Move it graphically.
            adapter?.notifyItemMoved(start, end)
            return false
        }
        /* Method Prototypes */
        /*
        * The method onSwipe will be called when an item gets swiped horizontally.
        *
        * @params
        *   @param viewHolder viewHolder: this recycler view's article view holder.
        *   @param direction Int: the movement direction.
        *
        *
        * @returns
        *
        */
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //We are only handling left swipes so we don't have to check direction.
            //Delete confirmation window.
            val eraseConfirmation = AlertDialog.Builder(myInterface as Context)
            eraseConfirmation.setMessage("Do you want to delete this item?")
                .setCancelable(false)
                .setPositiveButton("Delete") { _, _ -> //If user wishes to delete
                    //Get item position.
                    val position=viewHolder.absoluteAdapterPosition
                    //Remove this position from inventory.
                    articleViewModel.deleteFromInventory(position)
                    //Update graphically.
                    adapter?.notifyItemRemoved(position)
                }
                .setNegativeButton("Go back") { myDialog, _ -> //If user does not wish to delete.
                    //Get item position.
                    val position=viewHolder.absoluteAdapterPosition
                    //Dismiss dialog.
                    myDialog.dismiss()
                    //Return item to position graphically.
                    adapter?.notifyItemChanged(position)
                }
            //Show the delete confirmation
            eraseConfirmation.create().show()
        }

    }
}