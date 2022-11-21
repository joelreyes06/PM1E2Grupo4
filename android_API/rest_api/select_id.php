<?php

include 'connection.php';
$id_usuario = $_POST['id_usuario'];
$query = mysqli_query($con, "SELECT * FROM contacto WHERE id_usuario = '$id_usuario'");
$data = array();
$qry_array = array();
$i = 0;
$total = mysqli_num_rows($query);
while ($row = mysqli_fetch_array($query)) {
  $data['id_usuario'] = $row['id_usuario'];
  $data['nombre'] = $row['nombre'];
  $data['telefono'] = $row['telefono'];
  $data['latitud'] = $row['latitud'];
  $data['longitud'] = $row['longitud'];

  $qry_array[$i] = $data;
  $i++;
}

if($query){
  $response['success'] = 'true';
  $response['message'] = 'Dato cargado con extio';
  $response['total'] = $total;
  $response['data'] = $qry_array;
}else{
  $response['success'] = 'false';
  $response['message'] = 'Error al cargar el dato';
}

echo json_encode($response);
?>
