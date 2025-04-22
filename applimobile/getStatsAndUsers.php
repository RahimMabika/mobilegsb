<?php
header("Content-Type: application/json");

$host = "localhost";
$dbname = "frais_app";
$dbuser = "root";
$dbpassword = "";

try {
    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8mb4", $dbuser, $dbpassword);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    //  Statistiques
    $total = $pdo->query("SELECT COUNT(*) FROM fiches_frais")->fetchColumn();
    $attente = $pdo->query("SELECT COUNT(*) FROM fiches_frais WHERE statut = 'en attente'")->fetchColumn();
    $validees = $pdo->query("SELECT COUNT(*) FROM fiches_frais WHERE statut = 'accepté'")->fetchColumn();
    $refusees = $pdo->query("SELECT COUNT(*) FROM fiches_frais WHERE statut = 'refusé'")->fetchColumn();

    //  Utilisateurs
    $stmt = $pdo->query("SELECT id, email, role FROM users");
    $users = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
        "status" => 200,
        "total" => (int)$total,
        "attente" => (int)$attente,
        "validees" => (int)$validees,
        "refusees" => (int)$refusees,
        "users" => $users
    ]);

} catch (PDOException $e) {
    echo json_encode(["status" => 500, "message" => "Erreur BDD : " . $e->getMessage()]);
}
