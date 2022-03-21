/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
//    companion object with the timer constants
    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }

//    Add a CountDownTimer
    private val timer: CountDownTimer

    // Create a properly encapsulated LiveData for the current time called currentTime
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
            get() = _currentTime

//    Move over the word, score, and wordList variables to the GameViewModel
//    Set up word and score as MutableLiveData

    // The current word
    // internal and external version of word
        // internal
    private val _word = MutableLiveData<String>()
        // external
    val word: LiveData<String>
        // Getter
        get() = _word // (word = _word)

    // The current score
    // internal and external version of score
        // internal
    private val _score = MutableLiveData<Int>()
        //external
    val score: LiveData<Int>
        // Getter
        get() = _score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    // Make a properly encapsulated LiveData called eventGameFinish that holds a boolean
    // internal and external version of score
    // internal
    private val _eventGameFinish = MutableLiveData<Boolean>()
    //external
    val eventGameFinish: LiveData<Boolean>
        // Getter
        get() = _eventGameFinish

//    Make an init block that prints out a log saying “GameViewModel created!”
    init {
//        Log.i("GameViewModel", "GameViewModel created!")
        // Move over this initialization to the GameViewModel
        _eventGameFinish.value = false
        resetList()
        nextWord()
        // Initialize score.value to 0
        _score.value = 0

        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                // TODO implement what should happen each tick of the timer
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                // TODO implement what should happen when the timer finishes
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }
        }

        timer.start()
    }

//    Override onCleared and prints out a log saying “GameViewModel destroyed!”
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
        // Cancel the timer in onCleared
        timer.cancel()
    }

//    Move methods resetList, nextWord, onSkip, and onCorrect to the GameViewModel

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }


    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            // The game should finish when the timer runs out NOT
                // when there are no words left in the list
            // If there are no words in the list, you should add
            // the words back to the list and re-shuffle the list
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/
    // change references and add null safety checks
    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /** Methods for completed events **/
    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

}