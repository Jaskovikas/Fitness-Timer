package edu.ktu.fitnessapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import edu.ktu.fitnessapp.databinding.FragmentSessionBinding
import kotlin.math.roundToInt


class SessionFragment : Fragment() {

    private var timerPaused = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binder = FragmentSessionBinding.inflate(inflater, container, false)

        val viewModel : SetViewModel by activityViewModels()

        binder.viewmodel = viewModel
        binder.lifecycleOwner = this

        var workoutSets =  ArrayList(viewModel.sets.value)

        startGetReadyCountdown(binder, workoutSets)



        //Log.d("LiveData", viewModel.sets.value.toString())

        return binder.root
    }


    private fun startGetReadyCountdown(binder: FragmentSessionBinding, workoutSets: ArrayList<Set>) {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished % 60000) / 1000 + 1
                binder.getrdcountdownTxt.text = secondsRemaining.toString()
            }
            override fun onFinish() {
                startWorkout(binder, workoutSets)
            }
        }.start()
    }

    private fun startWorkout(binder: FragmentSessionBinding, workoutSets: ArrayList<Set>) {

        unhideElements(binder)


        //set initial

        startExerciseTimer(binder, workoutSets)
    }

    private fun changeCurAndNextText(binder: FragmentSessionBinding,
                                     workoutSets: ArrayList<Set>,
                                     setIndex: Int,
                                     exerciseIndex: Int,
                                     setCount: Int) {

        binder.currentExerciseText.text = workoutSets[setIndex].exercises[exerciseIndex].name
        binder.setNumberTxt.text = "Set #${setIndex + 1}"
        binder.setCountTxt.text = "Count: ${setCount}/${workoutSets[setIndex].count}"
        if (workoutSets[setIndex].exercises.size > exerciseIndex + 1)
            binder.nextExerciseText.text = workoutSets[setIndex].exercises[exerciseIndex + 1].name
        else if (workoutSets[setIndex].count > setCount)
            binder.nextExerciseText.text = workoutSets[setIndex].exercises[0].name
        else if (workoutSets.size > setIndex + 1)
            binder.nextExerciseText.text = workoutSets[setIndex + 1].exercises[0].name
        else
            binder.nextExerciseText.text = "-"
    }

    private fun startExerciseTimer(binder: FragmentSessionBinding,
                                   workoutSets: ArrayList<Set>) {


        var millisLeft : Long = 0

        var currentSetIndex = 0
        var currentExerciseIndex = 0
        var currentSetCountIndex = 1


        var timer : CountDownTimer

        fun timerStart(binder: FragmentSessionBinding,
                       workoutSets: ArrayList<Set>,
                       setIndex: Int, exerciseIndex: Int,
                       setCount: Int,
                       remainingTime: Long): CountDownTimer {

            changeCurAndNextText(binder, workoutSets, setIndex, exerciseIndex, setCount)
            val totalTimeInMillis = calculateTimeInMillis(workoutSets[setIndex]
                    .exercises[exerciseIndex].timeText)
            //Log.d("Current set:", currentSetIndex.toString())
            //Log.d("Current exerc:", currentExerciseIndex.toString())
            //Log.d("Current set count:", currentSetCountIndex.toString())


            val timer = object : CountDownTimer(remainingTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {


                    millisLeft = millisUntilFinished
                    binder.progressBar.progress = (millisUntilFinished.toDouble() /
                            totalTimeInMillis * 100).roundToInt()
                    //Log.d("Progress:", binder.progressBar.progress.toString())
                    val minutesRemaining = millisUntilFinished / 60000
                    val secondsRemaining = (millisUntilFinished % 60000) / 1000
                    val minutes = appendZero(minutesRemaining)
                    val seconds = appendZero(secondsRemaining + 1)
                    binder.timeRemainingText.text = "${minutes} : ${seconds}"
                }

                override fun onFinish() {
                    binder.timeRemainingText.text = "00 : 00"
                    binder.progressBar.progress = 0
                    var nextExercTime : Long = 0
                    millisLeft = nextExercTime
                    if (workoutSets[setIndex].exercises.size > exerciseIndex + 1) {
                         nextExercTime = calculateTimeInMillis(workoutSets[setIndex].
                            exercises[exerciseIndex +1].timeText)
                        currentExerciseIndex++
                        timer=timerStart(binder, workoutSets, setIndex,
                                exerciseIndex + 1,
                                setCount,
                                 nextExercTime)


                    }
                    else if (workoutSets[setIndex].count > setCount) {
                        nextExercTime = calculateTimeInMillis(workoutSets[setIndex].
                        exercises[0].timeText)
                        currentExerciseIndex = 0
                        currentSetCountIndex++
                        timer=timerStart(binder, workoutSets, setIndex,
                                0, setCount + 1,
                                nextExercTime)



                    }
                    else if (workoutSets.size > setIndex + 1) {
                        nextExercTime = calculateTimeInMillis(workoutSets[setIndex + 1]
                                .exercises[0].timeText)
                        currentExerciseIndex = 0
                        currentSetCountIndex = 1
                        currentSetIndex++
                        timer=timerStart(binder, workoutSets, setIndex + 1, 0,
                                1, nextExercTime)



                    }
                    else {
                        //workout finished
                        val action = SessionFragmentDirections.actionSessionFragmentToFinishFragment()
                        binder.root.findNavController().navigate(action)
                    }

                }

            }.start()
            return timer;
        }

        val timeForFirst = calculateTimeInMillis(workoutSets[currentSetIndex].
        exercises[currentExerciseIndex].timeText)
        timer = timerStart(binder, workoutSets, currentSetIndex, currentExerciseIndex,
                currentSetCountIndex, timeForFirst)

        binder.pausePlayBtn.setOnClickListener {
            if (!timerPaused) {
                timer.cancel()
                binder.pausePlayBtn.setIconResource(R.drawable.ic_baseline_play_circle_outline_90)
                timerPaused = true
            } else {
                timer = timerStart(binder, workoutSets, currentSetIndex, currentExerciseIndex,
                        currentSetCountIndex, millisLeft)
                binder.pausePlayBtn.setIconResource(R.drawable.ic_baseline_pause_circle_outline_90)
                timerPaused = false
            }
        }

        binder.prevExercBtn.setOnClickListener {
            var nextExercTime : Long
            if (currentSetIndex != 0 ||  currentExerciseIndex != 0 || currentSetCountIndex > 1) {
                timer.cancel()
                binder.pausePlayBtn.setIconResource(R.drawable.ic_baseline_pause_circle_outline_90)
                timerPaused = false
                if (currentExerciseIndex != 0) {

                    nextExercTime = calculateTimeInMillis(workoutSets[currentSetIndex]
                            .exercises[currentExerciseIndex - 1].timeText)
                    currentExerciseIndex--
                    timer = timerStart(binder, workoutSets, currentSetIndex, currentExerciseIndex,
                            currentSetCountIndex, nextExercTime)
                }
                else if (currentExerciseIndex == 0 && currentSetCountIndex > 1) {

                    nextExercTime = calculateTimeInMillis(workoutSets[currentSetIndex]
                            .exercises.last().timeText)

                    currentExerciseIndex = workoutSets[currentSetIndex].exercises.lastIndex
                    currentSetCountIndex--

                    timer = timerStart(binder, workoutSets, currentSetIndex, currentExerciseIndex,
                            currentSetCountIndex, nextExercTime)
                } else {

                    nextExercTime = calculateTimeInMillis(workoutSets[currentSetIndex - 1]
                            .exercises[0].timeText)
                    currentSetIndex--
                    currentExerciseIndex = workoutSets[currentSetIndex].exercises.lastIndex
                    currentSetCountIndex = workoutSets[currentSetIndex].count
                    timer = timerStart(binder, workoutSets, currentSetIndex, currentExerciseIndex,
                            currentSetCountIndex, nextExercTime)
                }
            }
        }

        binder.nextExerciseBtn.setOnClickListener {
            var nextExercTime : Long
            if (currentSetIndex != workoutSets.lastIndex ||  currentExerciseIndex != workoutSets[currentSetIndex].
                    exercises.lastIndex || currentSetCountIndex < workoutSets.last().count) {
                timer.cancel()
                binder.pausePlayBtn.setIconResource(R.drawable.ic_baseline_pause_circle_outline_90)
                timerPaused = false
                //if has more than one exercise in a set
                if (currentExerciseIndex != workoutSets[currentSetIndex].exercises.lastIndex
                   ) {

                    nextExercTime = calculateTimeInMillis(workoutSets[currentSetIndex]
                            .exercises[currentExerciseIndex + 1].timeText)
                    currentExerciseIndex++
                    timer = timerStart(binder, workoutSets, currentSetIndex, currentExerciseIndex,
                            currentSetCountIndex, nextExercTime)
                }
                //if at the last set item but has count left
                else if (currentExerciseIndex == workoutSets[currentSetIndex].exercises.lastIndex
                        && currentSetCountIndex < workoutSets[currentSetIndex].count) {

                    nextExercTime = calculateTimeInMillis(workoutSets[currentSetIndex]
                            .exercises.first().timeText)

                    currentExerciseIndex = 0
                    currentSetCountIndex++

                    timer = timerStart(binder, workoutSets, currentSetIndex, currentExerciseIndex,
                            currentSetCountIndex, nextExercTime)
                } else {

                    nextExercTime = calculateTimeInMillis(workoutSets[currentSetIndex + 1]
                            .exercises[0].timeText)
                    currentSetIndex++
                    currentExerciseIndex = 0
                    currentSetCountIndex = 1
                    timer = timerStart(binder, workoutSets, currentSetIndex, currentExerciseIndex,
                            currentSetCountIndex, nextExercTime)
                }
            }
        }


    }

    private fun calculateTimeInMillis(text : String): Long {
        val exerciseTime = text
        val minAndSecList = exerciseTime.split(":").toTypedArray()
        val timeInSeconds = minAndSecList[0].toLong() * 60 + minAndSecList[1].toLong()
        return timeInSeconds * 1000
    }

    private fun appendZero(number: Long): String {
        return String.format("%02d", number)
    }

    private fun unhideElements(binder: FragmentSessionBinding) {

        //hide get ready text
        binder.getreadytext.visibility = View.GONE
        binder.getrdcountdownTxt.visibility = View.GONE

        //unhide all other elements

        binder.nowdoing.visibility = View.VISIBLE
        binder.nexttext.visibility = View.VISIBLE
        binder.progressBar.visibility = View.VISIBLE
        binder.timeRemainingText.visibility = View.VISIBLE
        binder.pausePlayBtn.visibility = View.VISIBLE
        binder.prevExercBtn.visibility = View.VISIBLE
        binder.nextExerciseBtn.visibility = View.VISIBLE
        binder.nextExerciseText.visibility = View.VISIBLE
        binder.currentExerciseText.visibility = View.VISIBLE
        binder.nexttext.visibility = View.VISIBLE
        binder.setNumberTxt.visibility = View.VISIBLE
        binder.setCountTxt.visibility = View.VISIBLE

    }



}