package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
     fun CadenaConexion():Connection? {
         try {
             val url = "jdbc:oracle:thin:@10.10.3.164:1521:xe"
              val usuario = "SYSTEM"
              val contraseña = "pituco01"

              val connection = DriverManager.getConnection(url, usuario, contraseña)
              return connection
         }catch(e: Exception){
              println("error: $e")
              return null
         }
     }
}