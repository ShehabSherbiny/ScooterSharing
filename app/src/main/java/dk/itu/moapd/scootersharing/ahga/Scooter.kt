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
package dk.itu.moapd.scootersharing.ahga

import java.text.DateFormat

/**
 * This class is a data class meant to hold information about scooters
 * @property name is a String; Name of the scooter
 * @property location is a String; Starting point for the ride
 * @property timestamp Is a long; Timestamp
 */
data class Scooter(val name: String, var location: String, var timestamp: Long = System.currentTimeMillis()){
    override fun toString(): String {
        return "Scooter '$name' at '$location' on ${DateFormat.getDateInstance().format(timestamp)}"
    }
}

