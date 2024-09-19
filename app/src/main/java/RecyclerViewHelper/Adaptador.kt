package RecyclerViewHelper

import alejandro.menendez.crud_carpinteria.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.ClaseConexion
import modelo.tb_Carpintero



class Adaptador( private var Datos : List<tb_Carpintero>): RecyclerView.Adapter<ViewHolder>() {


    fun actualizarCarpintero(nuevaLista: List<tb_Carpintero>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }
    fun eliminarCarpintero( posicion: Int) {
        //Eliminarlo de la lista
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)
        GlobalScope.launch(Dispatchers.IO) {
            //1- Creo un objeto de la clase conexion
            val objConexion = ClaseConexion().CadenaConexion()

            //2- creo una variable que contenga
            //un PrepareStatement
            val deleteCarpintero =
                objConexion?.prepareStatement("delete from tbCarpintero   where UUID_Carpintero = ?")!!
            deleteCarpintero.setString(1,posicion.toString())
            deleteCarpintero.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

     fun actualizarCarpintero(nuevoCarpintero: String, nuevaEdad: String, nuevoPeso:String, nuevoCorreo:String, uuid: String) {
        GlobalScope.launch(Dispatchers.IO) {
            //1- Creo un obj de la clase conexion
            val objConexion = ClaseConexion().CadenaConexion()

            //2- Creo una variable que contenga un PrepareStatement
            val updateCarpintero = objConexion?.prepareStatement("update tbCarpintero set Nombre_Carpintero = ?,  Edad_Carpintero = ? , Peso_Carpintero = ? , Correo_Carpintero = ?  where UUID_Carpintero = ?")!!
            updateCarpintero.setString(1, nuevoCarpintero)
            updateCarpintero.setString(2, nuevaEdad)
            updateCarpintero.setDouble(3, nuevoPeso.toDouble())
            updateCarpintero.setString(4,nuevoCorreo)
            updateCarpintero.setString(5, uuid)
            updateCarpintero.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val item = Datos[position]
        holder.txtNombreCard.text = item.Nombre_Carpintero
        holder.txtNombreCard2.text = item.Edad_Carpintero.toString()
        holder.txtNombreCard3.text = item.Peso_Carpintero.toString()
        holder.txtNombreCard4.text = item.Correo_Carpintero
        holder.imgEliminar.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Confirmacion")
            builder.setMessage("Estas seguro que quieres borrar")

            builder.setPositiveButton("Si"){ dialog, wich ->
                eliminarCarpintero(position)
            }
            builder.setNegativeButton("No"){ dialog, wich ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }
        holder.imgEditar.setOnClickListener {
            //Creo mi Alerta para editar
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Actualizar")

            //Agregarle un cuadro de texto
            //donde el usuario va a escribir el nuevo nombre
            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL

            val nombreEditText = EditText(context).apply { setHint("Nombre: ${item.Nombre_Carpintero}") }
            val edadEditText = EditText(context).apply { setHint("Edad: ${item.Edad_Carpintero}") }
            val pesoEditText = EditText(context).apply { setHint("Peso: ${item.Peso_Carpintero}") }
            val correoEditText = EditText(context).apply { setHint("Correo: ${item.Correo_Carpintero}") }

            layout.addView(nombreEditText)
            layout.addView(edadEditText)
            layout.addView(pesoEditText)
            layout.addView(correoEditText)

            builder.setView(layout)




            //Botones
            builder.setPositiveButton("Actualizar"){
                    dialog, wich ->
               actualizarCarpintero(nombreEditText.text.toString(), edadEditText.text.toString(), item.Peso_Carpintero.toString(), item.Correo_Carpintero, item.UUID_Carpintero)

            }
            builder.setNegativeButton("Cancelar"){
                    dialog, wich ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }
        }
}



