package com.example.fingerprintvotingsystem

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.fingerprintvotingsystem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var hasVoted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BJP.setOnClickListener {
            authenticateAndVote("BJP")
        }

        binding.INC.setOnClickListener {
            authenticateAndVote("INC")
        }

        binding.JDU.setOnClickListener {
            authenticateAndVote("JDU")
        }

        binding.SP.setOnClickListener {
            authenticateAndVote("SP")
        }
    }

    private fun authenticateAndVote(partyName: String) {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val executor = ContextCompat.getMainExecutor(this)
                val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        showErrorDialog("Authentication error: $errString")
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        handleVote(partyName)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        showErrorDialog("Authentication failed")
                    }
                })

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Fingerprint Authentication")
                    .setSubtitle("Authenticate to cast your vote")
                    .setNegativeButtonText("Cancel")
                    .build()

                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> showErrorDialog("No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> showErrorDialog("Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> showErrorDialog("No biometric credentials are currently enrolled.")
        }
    }

    private fun handleVote(partyName: String) {
        if (hasVoted) {
            showAlreadyVotedDialog()
        } else {
            hasVoted = true
            showVotingResultDialog(partyName)
        }
    }

    private fun showVotingResultDialog(partyName: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Voting Result")
        builder.setMessage("Your vote for $partyName was successful!")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showAlreadyVotedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("You have already voted!")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showErrorDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}
