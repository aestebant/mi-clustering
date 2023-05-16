package jclec.selector;

import jclec.IIndividual;
import jclec.ISystem;
import jclec.fitness.IValueFitness;

/*
 * Nombre:EscalamientoSigmaSelector
 * Autor: Rafael AyllA?n Iglesias
 * Tipo: Clase publica
 * Extiende: La clase RouletteSelector y la interfaz IIndividual
 * Implementa: Nada
 * Variables de la clase: serialVersionUID (generado por eclipse)
 * Metodos: Protegidos: prepareSelection
 *          Publicos: Ninguno
 * Objetivo de la clase: Esta clase pretende implementar el selector mediante
 *                       escalamiento sigma, con lo cual en esta clase en su metodo
 *                       prepareSelection se inicianilaran los datos de la ruleta
 *                       la cual se usara para realizar la seleccion.
 *
 */

/**
 * Sigma scaling selector.
 *
 * @author Rafael AyllA?n-Iglesias
 * @author SebastiA?n Ventura
 */

public class SigmaScaling extends RouletteSelector {
    /////////////////////////////////////////////////////////////////
    // -------------------------------------- Serialization constants
    /////////////////////////////////////////////////////////////////

    /**
     * Generated by Eclipse
     */

    private static final long serialVersionUID = -422501366558313344L;

    /////////////////////////////////////////////////////////////////
    // ------------------------------------------------- Constructors
    /////////////////////////////////////////////////////////////////

    /**
     * Empty (default constructor).
     */

    public SigmaScaling() {
        super();
    }

    /**
     * Constructor that contextualize selector
     *
     * @param context Execution context
     */

    public SigmaScaling(ISystem context) {
        super(context);
    }

    /////////////////////////////////////////////////////////////////
    // -------------------------------------------- Protected methods
    /////////////////////////////////////////////////////////////////

    /**
     * Nombre: prepareSelection
     * Autor: Rafael AyllA?n Iglesias.
     * Tipo funcion: Protegida
     * Valores de entrada: Ninguno
     * Valores de salida: Ninguna
     * Funciones que utiliza: Ninguna
     * Variables:- TotalAptitud es la aptitud total de la poblacion
     * - AptitudCuadrado es la aptitud total cuadratica de la poblacion
     * - media es la media del fitness de la poblacion
     * - TotalAptitudCuadrado es el cuadrado de la aptitud total
     * - sigma es la variable que almacena la funcion de reparte de las partes de la ruleta
     * - acc es el acumulado de las partes de la ruleta
     * - idx es el indice de la ruleta
     * Objetivo: Preparar las variables para la utilizacion de la tecnica de la ruleta
     */

    @Override
    protected void prepareSelection() {

        // Allocates space for roulette
        if ((roulette == null) || (roulette.length != actsrcsz)) {
            roulette = new double[actsrcsz];
        }

        //Calculo datos
        double TotalAptitud = 0.0;
        double AptitudCuadrado = 0.0;//exp(fi,2)
        for (IIndividual ind : actsrc) {
            // Fitness value for actual individual
            double val = ((IValueFitness) ind.getFitness()).getValue();
            TotalAptitud += val;
            AptitudCuadrado += (val + val);
        }
        double media = TotalAptitud / actsrcsz;
        double TotalAptitudCuadrado = TotalAptitud * TotalAptitud;

        double sigma = Math.sqrt(((actsrcsz * AptitudCuadrado) - TotalAptitudCuadrado) / actsrcsz * actsrcsz);

        // Sets roulette values
        if (sigma == 0) {
            int idx = 0;
            for (int i = 0; i < actsrcsz; i++) {
                roulette[idx++] = 1;
            }
        } else {
            double acc = 0.0;
            int idx = 0;
            for (IIndividual ind : actsrc) {
                // Fitness value for actual individual
                double val = ((IValueFitness) ind.getFitness()).getValue();
                // Calculate
                val = 1 + ((val - media) / (2 * sigma));
                // Update acc
                acc += val;
                // Set roulette value
                roulette[idx++] = acc;
            }
            // Normalize roulette values
            for (; idx > 0; ) {
                roulette[--idx] /= acc;
            }
        }
    }
}