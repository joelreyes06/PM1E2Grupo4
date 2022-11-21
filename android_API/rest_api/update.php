<?php
include 'connection.php';

$id_usuario = $_POST['id_usuario'];
$nombre = $_POST['nombre'];
$telefono = $_POST['telefono'];
$latitud = $_POST['latitud'];
$longitud = $_POST['longitud'];

$query = mysqli_query($con, "UPDATE contacto SET nombre = '$nombre', telefono = '$telefono', latitud = '$latitud', longitud = '$longitud' WHERE id_usuario = '$id_usuario'");

if($query){
  $response['success'] = 'true';
  $response['message'] = 'Dato actualizado con exito';
}else{
  $response['success'] = 'false';
  $response['message'] = 'Error al actualizar el dato';
}

echo json_encode($response);
?>
