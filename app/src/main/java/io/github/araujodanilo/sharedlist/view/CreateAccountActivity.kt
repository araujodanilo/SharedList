package io.github.araujodanilo.sharedlist.view


import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import io.github.araujodanilo.sharedlist.databinding.ActivityCreateAccountBinding

class CreateAccountActivity : BaseActivity() {
    private val acab: ActivityCreateAccountBinding by lazy {
        ActivityCreateAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acab.root)

        acab.createAccountBt.setOnClickListener{
            val email = acab.emailEt.text.toString()
            val password = acab.passwordEt.text.toString()
            val repeatPassword = acab.repeatPasswordEt.text.toString()

            if (password.equals(repeatPassword)){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener{
                    Toast.makeText(this@CreateAccountActivity,"Usuário $email criado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this@CreateAccountActivity,"Erro na criação do usuário!", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this@CreateAccountActivity,"Senha não coincidem!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}