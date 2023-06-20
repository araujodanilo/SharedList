package io.github.araujodanilo.sharedlist.view

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import io.github.araujodanilo.sharedlist.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : BaseActivity() {
    private val arpb: ActivityResetPasswordBinding by lazy{
        ActivityResetPasswordBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(arpb.root)

        arpb.sendEmailBt.setOnClickListener {
            val email = arpb.recoveryPasswordEmailEt.text.toString()
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { result ->
                if(result.isSuccessful){
                    Toast.makeText(this, "Email de recuperação enviado para $email", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this, "Falha no envio do email de recuperação $email", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}