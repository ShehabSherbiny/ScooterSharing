/*
MIT License

Copyright (c) [2023] [Ahmed Bassam Galal & Alyson De Souza]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package dk.itu.moapd.scootersharing.ahga.dataClasses

import java.text.DateFormat
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


// val itu = LatLng(55.6596, 12.5910)
@IgnoreExtraProperties
data class Scooter(
    val name: String? = null,
    var location: String? = null,
    var timestamp: Long? = System.currentTimeMillis(),
    val batteryLevel: Int = 100,
    val available: Boolean = true,
    var latitude: Double? = 55.6596,
    var longitude: Double? = 12.5910
    )
{
    @Exclude
    fun getScooterName(): String? {
        return name
    }

    @Exclude
    fun getLat(): Double {
        return latitude!!
    }

    @Exclude
    fun getLon(): Double {
        return longitude!!
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "location" to location,
            "timestamp" to timestamp,
            "batteryLevel" to batteryLevel,
            "available" to available,
            "latitude" to latitude,
            "longitude" to longitude
        )
    }

    override fun toString(): String {
        return "Scooter '$name' at '$location' on ${DateFormat.getDateInstance().format(timestamp)}"
    }

}