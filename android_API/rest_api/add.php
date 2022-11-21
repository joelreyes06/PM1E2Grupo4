<?php
include 'connection.php';

$nombre = $_POST['nombre'];
$telefono = $_POST['telefono'];
$latitud = $_POST['latitud'];
$longitud = $_POST['longitud'];

$response = array();
$query = mysqli_query($con, "INSERT INTO contacto (nombre, telefono, latitud, longitud) VALUES ('$nombre','$telefono','$latitud','$longitud')");

if($query){
  $response['success'] = 'true';
  $response['message'] = 'Datos agregado con exito';
}else{
  $response['success'] = 'false';
  $response['message'] = 'Error al agregar los datos';
}

echo json_encode($response);
?>
