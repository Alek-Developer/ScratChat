package com.gsixacademy.android.scratchat

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.gsixacademy.android.scratchat.R.string.default_web_client_id
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.concurrent.TimeUnit

class SignInActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 1
    }

    private var googleSignInClient : GoogleSignInClient? = null
    private var fireBaseAuth : FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // 1. Dodavanje Na Animacija so Nut-Circle!

        val nutAnimator = animateNut(nut_image, TimeUnit.SECONDS.toMillis(2))

        updateConstraint(R.layout.activity_sign_in)

        nutAnimator.start()



        fireBaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@SignInActivity,gso)

        signinBtn.setOnClickListener {
            signIn()
        }
    }

    // 2. Fun - Dodavanje Na Animacija so Nut-Circle! Od 69 do 93 linija.
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun updateConstraint(@LayoutRes id : Int) {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(this, id)
        newConstraintSet.applyTo(rootView)
        TransitionManager.beginDelayedTransition(rootView)

    }

    private fun animateNut(nut : ImageView?, orbitDuration : Long): ValueAnimator {
/// Values: 0,359 e brzinata i rotacijata na kruzenjeto na objektot.
        val anim = ValueAnimator.ofInt(0,359)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParameters = nut?.layoutParams as ConstraintLayout.LayoutParams
            layoutParameters.circleAngle = value.toFloat()
            nut.layoutParams = layoutParameters

            anim.duration = orbitDuration
            anim.interpolator = LinearInterpolator()
            anim.repeatMode = ValueAnimator.RESTART
            anim.repeatCount = ValueAnimator.INFINITE
        }
        return anim
    }



    private fun signIn() {
        val signInIntent = googleSignInClient!!.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    fireBaseAuthWithGoogle(account!!)
                } catch (e : ApiException) {
                    Log.e(TAG, "Google Sign In Failed $e")
                    Toast.makeText(this@SignInActivity, "Google Sign in failed", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
    private fun fireBaseAuthWithGoogle(account : GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        fireBaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this@SignInActivity) { task ->

                if (task.isSuccessful) {
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SignInActivity, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}