package com.sample.currencyconversion.core.data.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log

class MyScheduler(private val context: Context) {

    private lateinit var jobScheduler: JobScheduler

    fun scheduleBackgroundJob(): Int {
        jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(JOB_ID, ComponentName(context, MyJobService::class.java))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // Network required
            .setPeriodic(THIRTY_MINUTES)
            .build()

        val resultCode = jobScheduler.schedule(jobInfo)
        return if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("JobScheduler", "Job scheduled successfully!")
        } else {
            Log.w("JobScheduler", "Job scheduling failed with code: $resultCode")
        }
    }

    fun cancelJob() {
        jobScheduler.cancel(JOB_ID)
    }

    companion object {
        const val JOB_ID = 10000
        const val THIRTY_MINUTES = 15 * 60 * 1000L
    }
}

