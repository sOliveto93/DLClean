package com.example.demo.dto;

public class TotalesCajaDto {
        private double totalEfectivo;
        private double totalMP;
        private double totalTarjetas;
        private double totalTransferencias;
        private double totalSistema; // suma de todas las anteriores

        public TotalesCajaDto(double totalEfectivo, double totalMP, double totalTarjetas, double totalTransferencias) {
            this.totalEfectivo = totalEfectivo;
            this.totalMP = totalMP;
            this.totalTarjetas = totalTarjetas;
            this.totalTransferencias = totalTransferencias;
            this.totalSistema = totalEfectivo + totalMP + totalTarjetas + totalTransferencias;
        }

        // Getters
        public double getTotalEfectivo() { return totalEfectivo; }
        public double getTotalMP() { return totalMP; }
        public double getTotalTarjetas() { return totalTarjetas; }
        public double getTotalTransferencias() { return totalTransferencias; }
        public double getTotalSistema() { return totalSistema; }


}
