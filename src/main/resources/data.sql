
INSERT INTO patients (id, firstName, lastName ) VALUES (1, 'Aknur', 'Mazhitova');
INSERT INTO patients (id, firstName, lastName ) VALUES (2, 'Gulzira', 'Nazaralina');

INSERT INTO prescriptions (id, patient_id, doctor_name, medication, dosage, issue_date, valid_until)
VALUES (1, 1, 'Dr. Aiym', 'Amoxicillin', '500mg twice daily', '2026-01-01', '2026-01-31');

INSERT INTO prescriptions (id, patient_id, doctor_name, medication, dosage, issue_date, valid_until)
VALUES (2, 1, 'Dr. Sanzhar', 'Ibuprofen', '200mg as needed', '2026-01-05', '2026-02-05');

