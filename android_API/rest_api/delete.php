<?php
include 'connection.php';

$id_usuario = $_POST['id_usuario'];

$query = mysqli_query($con, "DELETE FROM contacto WHERE id_usuario = '$id_usuario' ");

if($query){
  $response['success'] = 'true';
  $response['message'] = 'Dato eliminado con exito';
}else{
  $response['success'] = 'false';
  $response['message'] = 'Error al eliminar el dato';
}

echo json_encode($response);
?>
