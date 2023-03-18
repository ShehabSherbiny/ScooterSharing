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
package dk.itu.moapd.scootersharing.ahga.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import dk.itu.moapd.scootersharing.ahga.dataClasses.RidesDB
import dk.itu.moapd.scootersharing.ahga.databinding.ActivityMainBinding

/**
 * An activity class used as canvas for different Fragments and a ListView.
 */
class MainActivity : AppCompatActivity() {

    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * A set of static attributes used in this activity class.
     * `ridesDB` represents a MockDatBase, and is lazy instantiated.
     */
    companion object {
        lateinit var ridesDB: RidesDB
    }

    /**
     * Called when the activity is starting.
     * Derived classes must call through to the super class's implementation of this method.
     * If they do not, an exception will be thrown.
     *
     * `binding` is initialized.
     * `ridesDB` is initialized.
     * `setContentView(int)` inflates the activity's UI, using ViewBinding.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     * down then this Bundle contains the data it most recently supplied in `onSaveInstanceState()`.
     * Note: Otherwise it is null.
     */
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState:Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        ridesDB = RidesDB.get(this)

        setContentView(binding.root)
    }
}