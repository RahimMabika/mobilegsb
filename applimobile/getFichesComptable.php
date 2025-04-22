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

$filtre = $_GET['filtre'] ?? 'toutes';

if ($filtre === "attente") {
    $sql = $pdo->prepare("
        SELECT f.id, f.date, f.montant, f.statut, u.email
        FROM fiches_frais f
        JOIN users u ON f.user_id = u.id
        WHERE f.statut = 'en attente'
        ORDER BY f.date DESC
    ");
} else {
    $sql = $pdo->prepare("
        SELECT f.id, f.date, f.montant, f.statut, u.email
        FROM fiches_frais f
        JOIN users u ON f.user_id = u.id
        ORDER BY f.date DESC
    ");
}

$sql->execute();
$fiches = $sql->fetchAll(PDO::FETCH_ASSOC);

echo json_encode(["status" => 200, "fiches" => $fiches]);
