// AITA System - Group 5 Git Analytics Script (FE-L-03)

document.addEventListener('DOMContentLoaded', () => {
    console.log('AITA Git Analytics Engine Initialized.');
});

// Trigger repository sync via AJAX
function syncGitRepository(groupId) {
    const syncBtn = document.getElementById('btnSyncRepo');
    if (syncBtn) {
        syncBtn.innerText = 'Syncing...';
        syncBtn.disabled = true;
    }

    fetch('api/analytics/sync?groupId=' + groupId, {
        method: 'POST',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'SUCCESS') {
            alert('Git Repository metrics synchronized successfully!');
            window.location.reload();
        } else {
            alert('Error syncing repository: ' + (data.error || 'Unknown error'));
        }
    })
    .catch(err => {
        console.error(err);
        alert('Network or server error during Git sync.');
    })
    .finally(() => {
        if (syncBtn) {
            syncBtn.innerText = '🔄 Sync Git Repo';
            syncBtn.disabled = false;
        }
    });
}

// Open Lecturer score adjustment modal
function openAssessmentModal(userId, userName, currentScore, isFreerider) {
    document.getElementById('modalUserId').value = userId;
    document.getElementById('modalStudentName').innerText = userName;
    document.getElementById('modalScore').value = currentScore || 10.0;
    document.getElementById('modalFreerider').checked = isFreerider === true || isFreerider === 'true';
    document.getElementById('modalNotes').value = '';

    const modal = document.getElementById('assessmentModal');
    if (modal) {
        modal.classList.add('active');
    }
}

function closeAssessmentModal() {
    const modal = document.getElementById('assessmentModal');
    if (modal) {
        modal.classList.remove('active');
    }
}

// Submit Lecturer Assessment Override
function submitAssessmentForm(event) {
    event.preventDefault();

    const userId = document.getElementById('modalUserId').value;
    const score = document.getElementById('modalScore').value;
    const isFreerider = document.getElementById('modalFreerider').checked;
    const notes = document.getElementById('modalNotes').value;
    const groupId = 1;

    const params = new URLSearchParams();
    params.append('groupId', groupId);
    params.append('userId', userId);
    params.append('finalScore', score);
    params.append('isFreeriderFlagged', isFreerider);
    params.append('teacherNotes', notes);

    fetch('api/assessment/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        },
        body: params
    })
    .then(res => res.json())
    .then(data => {
        if (data.status === 'SUCCESS') {
            alert('Evaluation score saved successfully!');
            closeAssessmentModal();
            window.location.reload();
        } else {
            alert('Error saving assessment: ' + (data.error || 'Failed'));
        }
    })
    .catch(err => {
        console.error(err);
        alert('Failed to send evaluation update.');
    });
}
