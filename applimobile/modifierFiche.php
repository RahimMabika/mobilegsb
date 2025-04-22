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

if (!isset($_POST["id"]) || !isset($_POST["date"]) || !isset($_POST["montant"])) {
    echo json_encode(["status" => 400, "message" => "Champs manquants"]);
    exit();
}

$id = $_POST["id"];
$date = $_POST["date"];
$montant = $_POST["montant"];

$stmt = $pdo->prepare("UPDATE fiches_frais SET date = :date, montant = :montant WHERE id = :id");
$stmt->execute([
    "date" => $date,
    "montant" => $montant,
    "id" => $id
]);

echo json_encode(["status" => 200, "message" => "Fiche modifiée"]);
