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
    echo json_encode(["status" => 500, "message" => "Erreur de connexion"]);
    exit();
}

if (!isset($_POST['id']) || !isset($_POST['statut'])) {
    echo json_encode(["status" => 400, "message" => "Champs manquants"]);
    exit();
}

$id = $_POST['id'];
$statut = $_POST['statut'];

$stmt = $pdo->prepare("UPDATE fiches_frais SET statut = :statut WHERE id = :id");
$stmt->execute([
    'statut' => $statut,
    'id' => $id
]);

echo json_encode(["status" => 200, "message" => "Statut mis à jour"]);
