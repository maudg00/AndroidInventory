package com.example.posesionista
// Article.kt
//
//  The Article class represents a single article.
//  Created by Mauricio de Garay Hernández on 24/10/2021.
//
//

//Dependencies
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
private const val TAG="DateTest"
class Article():Parcelable{
    /* Attribute Prototypes. */
    //The article name.
    var name:String=""
    //The article cost.
    var cost:Int=0
    //The article serial number.
    var serialNumber:String=""
    //The article creation date.
    var creationDate: String=""
    //The article id, for picture purposes.
    var articleID = UUID.randomUUID().toString().substring(0,6)
    /* Method Prototypes. */

    /*
    * The init method will initialize the date attribute by parsing it properly.
    *
    * @params
    *
    *
    * @returns
    *
    *
    */
    init{
        //Parse date in dd/MM/yyyy using a SimpleDateFormat.
        val dateFormat = SimpleDateFormat(
            "dd/MM/yyyy", Locale.US
        )
        //Use this format to apply it to current date.
        creationDate=dateFormat.format(Date())
        Log.d(TAG, creationDate)
    }
    /*
    * The constructor method will define the Parcel reading definitions.
    *
    * @params
    *   @param parcel Parcel: the required parcel.
    *
    * @returns
    *
    *
    */
    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        cost = parcel.readInt()
        serialNumber=parcel.readString().toString()
        creationDate=parcel.readString().toString()
        articleID=parcel.readString().toString()
    }
    /*
    * The writeToParcel method will define the Parcel writing definitions.
    *
    * @params
    *   @param parcel Parcel: the required parcel.
    *   @param flags Int: the writing flags.
    *
    * @returns
    *
    *
    */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(cost)
        parcel.writeString(serialNumber)
        parcel.writeString(creationDate)
        parcel.writeString(articleID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }


}