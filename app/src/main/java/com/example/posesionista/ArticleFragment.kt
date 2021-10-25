package com.example.posesionista
// ArticleFragment.kt
//
//  The ArticleFragment class is the one responsible for handling our single article fragment.
//  Created by Mauricio de Garay Hernández on 24/10/2021.
//
//

//Dependencies
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

class ArticleFragment: Fragment(){
    /* Attribute Prototypes. */
    //The article is our selected article that we want to display.
    private lateinit var article:Article
    //The nameField is our article's name text view widget.
    private lateinit var nameField:EditText
    //The costField is our article's cost text view widget.
    private lateinit var costField:EditText
    //The serialField is our article's serial number text view widget.
    private lateinit var serialField:EditText
    //The dateField is our article's date text view widget.
    private lateinit var dateField:TextView
    //The photoView is our article's photo view widget.
    private lateinit var photoView:ImageView
    //The cameraButton is our camera button.
    private lateinit var cameraButton:ImageButton
    //The dateButton is our date picker button.
    private lateinit var dateButton:Button
    //Can we back press? used to validate name and serial entry.
    var allowBackPress:Boolean=false
    //Our picture file.
    private lateinit var pictureFile:File
    //Our camera result handler.
    private var cameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //If a camera result activity finished.
            output ->
        if(output.resultCode == Activity.RESULT_OK){
            //Set our photo view to use this picture file.
            output.data
            photoView.setImageBitmap(BitmapFactory.decodeFile(pictureFile.absolutePath))
        }
    }
    /* Method Prototypes. */

    /*
    * The onStart method is the one responsible for immediately initializing attributes and text view watchers.
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
        //If fields are empty, do not allow backpresses.
        allowBackPress = !(serialField.text.isEmpty() || nameField.text.isEmpty())
        //Text view watchers
        val myWatcher=object : TextWatcher{
            //Handle text view change events.
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when(p0.hashCode())
                {
                    nameField.text.hashCode()->article.name=p0.toString()
                    costField.text.hashCode()->{
                        if(p0!=null)
                        {
                            if(p0.isEmpty()) article.cost=0
                            else article.cost=p0.toString().toInt()
                        }

                    }
                    serialField.text.hashCode()->article.serialNumber= p0.toString()
                }
                //Only allow back press if both serial and name field are not empty
                allowBackPress = !(serialField.text.isEmpty() || nameField.text.isEmpty())
            }
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        }
        //Add this listener to our text views
        nameField.addTextChangedListener(myWatcher)
        costField.addTextChangedListener(myWatcher)
        serialField.addTextChangedListener(myWatcher)
        //Change action bar title.
        val activityBar=activity as AppCompatActivity
        activityBar.supportActionBar?.setTitle(R.string.article_detail)
        //Set camera button activity.
        cameraButton.apply{
            setOnClickListener{
                //If clicked, we want to start an intent with the camera activity.
                val intentPhoto= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                pictureFile = getPictureFile("${article.articleID}.jpg")
                val fileProvider = FileProvider.getUriForFile(context, "com.example.posesionista.fileprovider", pictureFile)
                intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                try{
                    cameraResult.launch(intentPhoto)
                }catch (e: ActivityNotFoundException){
                    //No camera found
                }
            }
        }
    }
    /*
    * The getPictureFile method is the one responsible for getting our File object for a taken picture.
    *
    * @params
    *   @param fileName String: the filename
    *
    * @returns
    *   File the file object.
    *
    */
    private fun getPictureFile(fileName:String) : File
    {
        //Lets get the complete filepath.
        val filePath = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(filePath, fileName)
    }
    /*
    * The onCreate method is the one called during this lifecycle event. It will read serialized article attributes.
    *
    * @params
    *   @param savedInstanceState Bundle?: the saved instance state.
    *
    * @returns
    *
    *
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Create the article.
        article=Article()
        //Load its parameters from serialized object.
        article=arguments?.getParcelable("RECEIVED_ARTICLE")!!

    }
    /*
    * The onCreateView method is the one called during this lifecycle event. It will inflate the fragment and initialize view attributes.
    *
    * @params
    *   @param inflater LayoutInflater: our layoutInflater.
    *   @param container ViewGroup: our container.
    *   @param savedInstanceState Bundle?: the savedInstanceState Bundle.
    *
    * @returns
    *   View: the inflated View.
    *
    *
    */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate
        val myView=inflater.inflate(R.layout.article_fragment, container, false)
        //Initialize fields.
        nameField=myView.findViewById(R.id.editName) as EditText
        costField=myView.findViewById(R.id.editCost) as EditText
        serialField=myView.findViewById(R.id.editSerial) as EditText
        dateField=myView.findViewById(R.id.editDate) as TextView
        nameField.setText(article.name)
        costField.setText(article.cost.toString())
        serialField.setText(article.serialNumber)
        dateField.text=article.creationDate
        //Set article photo if exists.
        photoView=myView.findViewById(R.id.imageArticle)
        pictureFile = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${article.articleID}.jpg")
        photoView.setImageBitmap(BitmapFactory.decodeFile(pictureFile.absolutePath))
        cameraButton=myView.findViewById(R.id.buttonCamera)
        dateButton=myView.findViewById(R.id.buttonDate)
        //Set date button on click listener.
        dateButton.setOnClickListener{ thisButton->
            onDatePickerClicked(thisButton)
        }
        //Return the view.
        return myView
    }
    /*
    * The onDatePickerClicked method will open a date picker dialog, and handle its events.
    *
    * @params
    *   @param button View: the pressed button.
    *
    * @returns
    *
    *
    */
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    fun onDatePickerClicked(@Suppress("UNUSED_PARAMETER")button: View)
    {
        //Lets get the current article date to start the date picker calendar widget there.
        //Split string, get values from array result.
        val dateParseString=article.creationDate.split("/")
        val day=dateParseString[0].toInt()
        //Months start in different index in date picker.
        val month=dateParseString[1].toInt()-1
        val year=dateParseString[2].toInt()
        //Start date picker dialog.
        val datePickerDialog=
            DatePickerDialog(
                activity as Context,
                { _, pickedYear, pickedMonth, pickedDay ->
                    //When picked, add it to date field and article attributes.
                    val pickedDate="${pickedDay}/${pickedMonth+1}/${pickedYear}"
                    dateField.text = pickedDate
                    this.article.creationDate=pickedDate
                },year,month,day,)
        //Do not allow future dates.
        datePickerDialog.datePicker.maxDate=System.currentTimeMillis()
        //Show the dialog.
        datePickerDialog.show()
    }
    companion object{
        /*
        * The newInstance method allows us to create a new instance for this object, while reading our serialized article which was clicked beforehand.
        *
        * @params
        *   @param article Article: the selected article.
        *
        * @returns
        *   ArticleFragment which was instantiated.
        *
        */
        fun newInstance(article:Article):ArticleFragment{
            //Check our serialized article.
            val myArguments= Bundle().apply{
                putParcelable("RECEIVED_ARTICLE", article)
            }
            //Return new ArticleFragment instance with this article's arguments.
            return ArticleFragment().apply {
                arguments=myArguments
            }
        }
    }
}