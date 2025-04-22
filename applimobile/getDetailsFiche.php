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
    echo json_encode(["status" => 500, "message" => "Erreur BDD"]);
    exit();
}

if (!isset($_GET['fiche_id'])) {
    echo json_encode(["status" => 400, "message" => "Paramètre fiche_id manquant"]);
    exit();
}

$fiche_id = $_GET['fiche_id'];

// Récupérer tous les détails liés à cette fiche
$stmt = $pdo->prepare("
    SELECT id, categorie, type, montant, date
    FROM details_frais
    WHERE fiche_id = :fiche_id
    ORDER BY type, date
");

$stmt->execute(['fiche_id' => $fiche_id]);
$frais = $stmt->fetchAll(PDO::FETCH_ASSOC);

echo json_encode([
    "status" => 200,
    "frais" => $frais
]);
