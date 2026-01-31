
-- =========================
-- ROLE
-- =========================
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    role_description VARCHAR(255)
);

-- =========================
-- USER
-- =========================
CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- USER_ROLE
-- =========================
CREATE TABLE user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);

-- =========================
-- PROFILE
-- =========================
CREATE TABLE profile (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    address TEXT,
    dob DATE,
    gender VARCHAR(20),
    profile_photo_url TEXT,
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- =========================
-- DOCUMENT STORE
-- =========================
CREATE TABLE document_store (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    file_name VARCHAR(255),
    file_type VARCHAR(50),
    file_url TEXT NOT NULL,
    size BIGINT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- =========================
-- PRESENTATION STORE
-- =========================
CREATE TABLE presentation_store (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    file_url TEXT NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- =========================
-- VIDEO STORE
-- =========================
CREATE TABLE video_store (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    file_url TEXT NOT NULL,
    duration INTEGER,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- =========================
-- THESIS REGISTRATION
-- =========================
CREATE TABLE thesis_registration (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT UNIQUE NOT NULL,
    school_name VARCHAR(255),
    gender VARCHAR(20),
    date_of_birth DATE,
    grade VARCHAR(20),
    section VARCHAR(20),
    thesis_topic VARCHAR(255),
    thesis_intent TEXT,
    has_digital_prototype BOOLEAN,
    status VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- =========================
-- PROOF OF CONCEPT
-- =========================
CREATE TABLE proof_of_concept (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT UNIQUE NOT NULL,
    content TEXT,
    status VARCHAR(30),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- =========================
-- DIGITAL PROTOTYPE
-- =========================
CREATE TABLE digital_prototype (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT UNIQUE NOT NULL,
    file_url TEXT,
    description TEXT,
    status VARCHAR(30),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- =========================
-- FINANCIAL MODEL
-- =========================
CREATE TABLE financial_model (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT UNIQUE NOT NULL,
    file_url TEXT NOT NULL,
    description TEXT,
    status VARCHAR(30),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- =========================
-- HELP REQUEST
-- =========================
CREATE TABLE help_request (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(100),
    description TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE help_request_comment (
    id BIGSERIAL PRIMARY KEY,
    request_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    comment TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES help_request(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- =========================
-- AUDIT LOG
-- =========================
CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE SET NULL
);

-- =========================
-- STUDENT SUBMISSION STORE (DASHBOARD)
-- =========================
CREATE TABLE student_submission_store (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT UNIQUE NOT NULL,
    school_name VARCHAR(255),
    grade VARCHAR(20),
    section VARCHAR(20),
    thesis_topic VARCHAR(255),
    registration_status VARCHAR(30),
    poc_status VARCHAR(30),
    digital_prototype_status VARCHAR(30),
    financial_model_status VARCHAR(30),
    presentation_status VARCHAR(30),
    selfie_video_status VARCHAR(30),
    overall_status VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- =========================
-- INDEXES
-- =========================
CREATE INDEX idx_school ON student_submission_store(school_name);
CREATE INDEX idx_grade ON student_submission_store(grade);
CREATE INDEX idx_topic ON student_submission_store(thesis_topic);
CREATE INDEX idx_status ON student_submission_store(overall_status);
