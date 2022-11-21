package com.example.pm1e2grupo4.Conexion;

public class conexion {
    public static final String URL = "http://192.168.0.15/";
    public static final String ROOT_URL = URL + "PM1E2Grupo4/rest_api/";

    public static final String URL_ADD = ROOT_URL + "add.php";
    public static final String URL_DELETE = ROOT_URL + "delete.php";
    public static final String URL_UPDATE = ROOT_URL + "update.php";
    public static final String URL_SELECT = ROOT_URL + "select.php";

    public static final String URL_SELECT_ID = ROOT_URL + "select_id.php";
}
