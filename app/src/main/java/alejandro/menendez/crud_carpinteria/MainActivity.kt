package alejandro.menendez.crud_carpinteria

import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.tb_Carpintero
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

     val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtEdad = findViewById<TextView>(R.id.txtEdad)
        val txtPeso = findViewById<TextView>(R.id.txtPeso)
        val txtCorreo = findViewById<TextView>(R.id.txtCorreo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        val rcvCarpinteria = findViewById<RecyclerView>(R.id.rcvCarpinteria)

        rcvCarpinteria.layoutManager = LinearLayoutManager(this)

        fun obtenerCarpinteros(): List<tb_Carpintero>{
            val objConexion = ClaseConexion().CadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery( "SELECT * FROM tbCarpintero")!!

            val listaCarpintero = mutableListOf<tb_Carpintero>()

            while (resultSet.next()){
                val UUID_Carpintero = resultSet.getString( "UUID_Carpintero")
                val Nombre_Carpintero = resultSet.getString( "Nombre_Carpintero")
                val Edad_Carpintero = resultSet.getInt( "Edad_Carpintero")
                val Peso_Carpintero = resultSet.getDouble( "Peso_Carpintero")
                val Correo_Carpintero = resultSet.getString( "Correo_Carpintero")

                val valoresJuntos = tb_Carpintero(UUID_Carpintero,Nombre_Carpintero, Edad_Carpintero, Peso_Carpintero,Correo_Carpintero)

                listaCarpintero.add(valoresJuntos)
            }
return listaCarpintero
        }

       CoroutineScope(Dispatchers.IO).launch {
           val CarpinterosDB = obtenerCarpinteros()
           withContext(Dispatchers.Main){
               val adapter = Adaptador(CarpinterosDB)
               rcvCarpinteria.adapter = adapter
           }
       }


        btnGuardar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().CadenaConexion()

                val addCarpintero = objConexion?.prepareStatement("insert into tbCarpintero (UUID_Carpintero, Nombre_Carpintero, Edad_Carpintero, Peso_Carpintero, Correo_Carpintero) values(?, ?, ?, ?, ?)")!!
                addCarpintero.setString( 1, UUID.randomUUID().toString())
                addCarpintero.setString( 2, txtNombre.text.toString())
                addCarpintero.setInt( 3, txtEdad.text.toString().toInt())
                addCarpintero.setDouble( 4, txtPeso.text.toString().toDouble())
                addCarpintero.setString( 5, txtCorreo.text.toString())
                addCarpintero.executeUpdate()

                val nuevoCarpintero = obtenerCarpinteros()
                withContext(Dispatchers.Main) {
                    (rcvCarpinteria.adapter as? Adaptador)?.actualizarCarpintero(nuevoCarpintero)
                }
            }
        }


    }
}