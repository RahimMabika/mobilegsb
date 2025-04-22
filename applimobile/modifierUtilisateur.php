<?php
header("Content-Type: application/json");

$host = "localhost";
$dbname = "frais_app";
$dbuser = "root";
$dbpassword = "";

try {
    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8mb4", $dbuser, $dbpassword);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    echo json_encode(["status" => 500, "message" => "Connexion BDD échouée"]);
    exit();
}

if (!isset($_POST["id"]) || (!isset($_POST["email"]) && !isset($_POST["password"]))) {
    echo json_encode(["status" => 400, "message" => "Champs manquants"]);
    exit();
}

$id = $_POST["id"];
$fields = [];

if (isset($_POST["email"])) {
    $fields["email"] = $_POST["email"];
}

if (isset($_POST["password"])) {
    $fields["password_hash"] = password_hash($_POST["password"], PASSWORD_DEFAULT);
}

if (count($fields) > 0) {
    $updates = [];
    foreach ($fields as $key => $value) {
        $updates[] = "$key = :$key";
    }

    $sql = "UPDATE users SET " . implode(", ", $updates) . " WHERE id = :id";
    $stmt = $pdo->prepare($sql);
    $fields["id"] = $id;
    $stmt->execute($fields);

    echo json_encode(["status" => 200, "message" => "Utilisateur mis à jour"]);
} else {
    echo json_encode(["status" => 400, "message" => "Aucune modification demandée"]);
}
