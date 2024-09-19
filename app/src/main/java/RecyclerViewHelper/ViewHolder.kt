package RecyclerViewHelper

import alejandro.menendez.crud_carpinteria.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

    var txtNombreCard = view.findViewById<TextView>(R.id.txtNombreCard)
    var txtNombreCard2 = view.findViewById<TextView>(R.id.txtNombreCard2)
    var txtNombreCard3 = view.findViewById<TextView>(R.id.txtNombreCard3)
    var txtNombreCard4 = view.findViewById<TextView>(R.id.txtNombreCard4)
    val imgEditar = view.findViewById<ImageView>(R.id.imgeditar)
    val imgEliminar = view.findViewById<ImageView>(R.id.imgeliminar)

}