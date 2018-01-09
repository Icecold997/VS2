;*********************************************************************
;* Hochschule für Technik und Wirtschaft                             *
;* Fakultät für Ingenieurwissenschaften							     *
;* Labor für Eingebettete Systeme 									 *
;* Mikroprozessortechnik		                        			 *
;*********************************************************************
;*                                                       		     *
;* atol.S: 	  										 			 *
;* Erhält Zeiger auf Ascii-String einer Dezimalzahl					 *
;* Rechnet um in Format unsigned long, Rückgabe des Werts in R0		 *
;*                                                       		     *
;*********************************************************************
;* Aufgabe-Nr.:   2.3       *	               						 *
;*              			*						               	 *
;*********************************************************************
;* Gruppen-Nr.: 			*										 *
;*              			*					               		 *
;*********************************************************************
;* Name / Matrikel-Nr.: Sander Timo / 3670635                        *
;*              		Born Thilo / 3670759   			 			 *
;*              			*						               	 *
;*********************************************************************
;* Abgabedatum:         18.12.2017     								 *
;*          	    		*					               		 *
;*********************************************************************

;*********************************************************************
;* Daten-Bereich 		       									     *
;*********************************************************************
	AREA     Daten, DATA, READWRITE

;*********************************************************************
;* Programm-Bereich                    								 *
;*********************************************************************
	AREA     Programm, CODE, READONLY
				   ARM
Reset_Handler  MSR      CPSR_c, #0x10	; User Mode aktivieren

;*********************************************************************
;* Hier das eigene Programm einfügen   								 *
;*********************************************************************
ATol	FUNCTION
	
	;; @param R0: Start eines Strings, Dezimalzahl als Asciizeichen,0 terminiert
	;;	Gültige Werte müssen in ein long passen (-2^31 - 2^31-1)
	;; @return: R0: Wert dargestellt als long
	
Start
		
atol		MOV		R1, R0 ;schreibzeiger
			MOV		R0, #0 ;R0 = ergebnis
			MOV		R2, #0xA ;; R2 = 10
			
				
loop1		LDRB	R4,[R1],#1	;;Lade ein Byte aus String_1 in R1, inkrementiere nach
									;;dem Laden die Speicheradresse in R1 um 1
			CMP		R4,#0x0		;;Abbruchbedingung string terminiert mit 0
            BEQ     checkNegativ			
			CMP     R4 ,#0x2D   ;;checken ob negative zahl
            MOVEQ   R5,#1       ;; wen negativ r5 auf 1 setzen
            BEQ     loop1		;; und nächste ziffer auslesen	 
			CMP     R4,#0x2B    ;; checken ob positive zahl
			BEQ     loop1       ;; wen positiv nächste ziffer auslesen
			
			SUB	R4, #0x30   ;; subtrahiert 30 von ascii  um zahl zu bekommen  (ascii 1 = 31)
			MLA	R0, R2, R0, R4  ;;  R0 = 10 * R0 + R4 
			B		loop1	    
			
checkNegativ 
            CMP    R5,#1   ;; checken ob zahl negativ war
			RSBEQ R0,R0,#0x00 ;; wen zahl negativ war subtrahiere positive von 0 
			
term		BX 		LR
			ENDFUNC
				
				
	EXPORT ATol[CODE]
    END